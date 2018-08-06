package de.unhandledexceptions.codersclash.bot.listeners;

import de.unhandledexceptions.codersclash.bot.core.Bot;
import de.unhandledexceptions.codersclash.bot.core.Database;
import de.unhandledexceptions.codersclash.bot.core.reactions.Reactions;
import de.unhandledexceptions.codersclash.bot.util.Messages;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashSet;
import java.util.Set;

import static de.unhandledexceptions.codersclash.bot.util.Messages.sendMessage;
import static java.lang.String.format;

/**
 * @author TheRealYann
 * 20.07.2018
 */

public class AutoChannelListener extends ListenerAdapter {

    private Set<VoiceChannel> channels = new HashSet<>();
    private Bot bot;

    public AutoChannelListener(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        var joined = event.getChannelJoined();
        if (joined.getIdLong() == bot.getCaching().getGuilds().get(event.getGuild().getIdLong()).getAuto_channel())
            createChannel(joined, event.getGuild(), event.getMember());
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        var left = event.getChannelLeft();
        if (channels.contains(left) && left.getMembers().isEmpty()) {
            left.delete().queue((v) -> channels.remove(left));
            if (!event.getGuild().getTextChannelsByName("channel-by-" + event.getMember().getUser().getName().toLowerCase(), false).isEmpty()) {
                event.getGuild().getTextChannelsByName("channel-by-" + event.getMember().getUser().getName().toLowerCase(), false).get(0).delete().queue();
            }
        }
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        var left = event.getChannelLeft();
        var joined = event.getChannelJoined();
        if (channels.contains(left) && left.getMembers().isEmpty())
            left.delete().queue((v) -> channels.remove(left));
        if (joined.getIdLong() == bot.getCaching().getGuilds().get(event.getGuild().getIdLong()).getAuto_channel())
            createChannel(joined, event.getGuild(), event.getMember());
    }

    private void createChannel(VoiceChannel channelJoined, Guild guild, Member member) {
        if (guild.getSelfMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            guild.getController().createVoiceChannel("Channel by " + member.getUser().getName())
                    .setUserlimit(channelJoined.getUserLimit())
                    .setParent(channelJoined.getParent())
                    .queue((channel) -> {
                        channels.add((VoiceChannel) channel);
                        guild.getController().moveVoiceMember(member, (VoiceChannel) channel).queue();
                        channel.createPermissionOverride(member).setAllow(Permission.ALL_CHANNEL_PERMISSIONS).queue();
                        guild.getController().createTextChannel("channel-by-" + member.getUser().getName())
                                .setTopic(format("This Channel is linked to the same-named Voice Channel %s %s (%s) by %s. Only the creator of the Voice Channel has permissions here.", Reactions.SPEAKER,
                                        channel.getName(), channel.getId(), member.getUser())).queue((textChannel) -> {
                                    textChannel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();
                                    textChannel.createPermissionOverride(member).setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();
                                }

                        );

                    });
        } else {
           PrivateChannel pc = member.getUser().openPrivateChannel().complete();
           sendMessage(pc, Messages.Type.ERROR, format("Woops. It seems like I don't have permission to do that on\n:satellite:*%s*!\n:x: **Manage Channels**", guild.getName()).queue();
        }
    }
}
