package de.unhandledexceptions.codersclash.bot.listeners;

import de.unhandledexceptions.codersclash.bot.core.Bot;
import de.unhandledexceptions.codersclash.bot.core.reactions.Reactions;
import de.unhandledexceptions.codersclash.bot.entities.AutoChannel;
import de.unhandledexceptions.codersclash.bot.util.Messages;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;

import static de.unhandledexceptions.codersclash.bot.util.Messages.sendMessage;
import static java.lang.String.format;

/**
 * @author TheRealYann
 * 20.07.2018
 */

public class AutoChannelListener extends ListenerAdapter {

    private HashMap<Long, AutoChannel> channels = new HashMap<>();
    private Bot bot;

    public AutoChannelListener(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        var joined = event.getChannelJoined();
        if (joined.getIdLong() == bot.getCaching().getGuilds().get(event.getGuild().getIdLong()).getAuto_channel()) {
            createChannel(joined, event.getGuild(), event.getMember());
        } else {
            if (event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL))
                if (channels.containsKey(joined.getIdLong()))
                    channels.get(joined.getIdLong()).getTextChannel()
                            .putPermissionOverride(event.getMember()).setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();
        }
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        var left = event.getChannelLeft();
        if (channels.containsKey(left.getIdLong()) && left.getMembers().isEmpty()) {
            left.delete().queue((v) -> channels.remove(left.getIdLong()));
            channels.get(left.getIdLong()).getTextChannel().delete().queue();
        }
        if (event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL))
            if (channels.containsKey(left.getIdLong()))
            channels.get(left.getIdLong()).getTextChannel()
                    .getManager().getChannel().getPermissionOverride(event.getMember()).delete().queue();
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        var left = event.getChannelLeft();
        var joined = event.getChannelJoined();
        if (channels.containsKey(left.getIdLong()) && left.getMembers().isEmpty()) {
            left.delete().queue((v) -> channels.remove(left.getIdLong()));
            channels.get(left.getIdLong()).getTextChannel().delete().queue();
        }
        if (joined.getIdLong() == bot.getCaching().getGuilds().get(event.getGuild().getIdLong()).getAuto_channel())
            createChannel(joined, event.getGuild(), event.getMember());
        else if (event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            if (channels.containsKey(left.getIdLong())) {
                if (channels.get(left.getIdLong()).getTextChannel().getManager().getChannel().getPermissionOverride(event.getMember()) != null) {
                    channels.get(left.getIdLong()).getTextChannel().getManager().getChannel().getPermissionOverride(event.getMember()).delete().queue();
                }
                channels.get(joined.getIdLong()).getTextChannel()
                        .putPermissionOverride(event.getMember()).setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();
            }
        }
    }

    private void createChannel(VoiceChannel channelJoined, Guild guild, Member member) {
        if (guild.getSelfMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            guild.getController().createVoiceChannel("Channel by " + member.getUser().getName())
                    .setUserlimit(channelJoined.getUserLimit())
                    .setParent(channelJoined.getParent())
                    .queue((channel) -> {
                        guild.getController().moveVoiceMember(member, (VoiceChannel) channel).queue();
                        channel.createPermissionOverride(member).setAllow(Permission.ALL_CHANNEL_PERMISSIONS).queue();
                        guild.getController().createTextChannel("channel by " + member.getUser().getName())
                                .setParent(channelJoined.getParent())
                                .setTopic(format("This Channel is linked to %s*%s* by %s.", Reactions.SPEAKER, channel.getName(),
                                        member.getUser())).queue((textChannel) -> {
                                            channels.put(channel.getIdLong(), new AutoChannel(channel.getIdLong(), textChannel.getIdLong(), member.getUser().getIdLong(), channelJoined.getJDA()));
                                            textChannel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();
                                            textChannel.createPermissionOverride(member).setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();
                                            sendMessage((MessageChannel) textChannel, Messages.Type.INFO,
                                                    format("To grant members access to this channel they need to join %s `%s` or %s can give them access to it!",
                                                            Reactions.SPEAKER, channel.getName(), member.getUser())).queue();
                                        });
                    });
        } else {
           PrivateChannel pc = member.getUser().openPrivateChannel().complete();
           sendMessage(pc, Messages.Type.ERROR, format("Woops. It seems like I don't have permission to do that on\n:satellite:*%s*!\n:x: **Manage Channels**", guild.getName())).queue();
        }
    }
}
