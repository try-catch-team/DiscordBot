package de.unhandledexceptions.codersclash.bot.commands;

import com.github.johnnyjayjay.discord.commandapi.CommandEvent;
import com.github.johnnyjayjay.discord.commandapi.CommandSettings;
import com.github.johnnyjayjay.discord.commandapi.ICommand;
import de.unhandledexceptions.codersclash.bot.core.Bot;
import de.unhandledexceptions.codersclash.bot.core.Database;
import de.unhandledexceptions.codersclash.bot.core.Permissions;
import de.unhandledexceptions.codersclash.bot.core.caching.Discord_guild;
import de.unhandledexceptions.codersclash.bot.core.caching.Discord_member;
import de.unhandledexceptions.codersclash.bot.core.caching.Discord_user;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageType;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static de.unhandledexceptions.codersclash.bot.util.Messages.*;
import static java.lang.String.format;

public class XPCommand extends ListenerAdapter implements ICommand {

    private static final long multiplicator = 32;


    private CommandSettings settings;
    private Bot bot;

    public XPCommand(CommandSettings settings, Bot bot) {
        this.settings = settings;
        this.bot = bot;
    }
    @Override
    public void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE))
            return;

        if (Permissions.getPermissionLevel(member) < 1) {
            noPermissionsMessage(channel, member);
        }

        if (event.getMessage().getMentionedMembers().size()==1) {
            member = event.getMessage().getMentionedMembers().get(0);
        }

        if (member.getUser().isBot()) {
            sendMessage(channel, Type.INFO, "Bots aren't allowed to use the XP Command!").queue();
            return;
        }

        Discord_guild guild = bot.getCaching().getGuilds().get(member.getGuild().getIdLong());
        Discord_member discord_member = bot.getCaching().getMember().get(member.getUser().getIdLong()+" "+member.getGuild().getIdLong());
        Discord_user discord_user = bot.getCaching().getUser().get(member.getUser().getIdLong());

        long memberXp = discord_member.getMember_xp();
        long userXp = discord_user.getUser_xp();
        long memberLevel = discord_member.getMember_lvl();
        long userLevel = discord_user.getUser_lvl();
        long maxUserXp = userLevel * multiplicator;
        long maxMemberXp = memberLevel * multiplicator;

        if (!guild.isXp_system_activated()) {
            sendMessage(channel, Type.WARNING, format("The XP-System is currently `deactivated`.\nUse `%ssettings` to re-enable it or contact your Server Admins.",
                    settings.getPrefix(member.getGuild().getIdLong()))).queue((msg) -> msg.delete().queueAfter(25, TimeUnit.SECONDS));
        } else if (event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_EMOTES)) {


            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .addField("Server", "Level: " + memberLevel +
                                    "\nXP: " + memberXp + "/" + maxMemberXp +
                                    "\n" + getProgressBar(memberXp, maxMemberXp, member)
                            ,true)
                    .addField("Global", "Level: "+userLevel +
                                    "\nXP: " + userXp + "/"+maxUserXp +
                                    "\n" + getProgressBar(userXp, maxUserXp, member)
                            , true);
            sendMessage(channel, Type.DEFAULT, "Take a look at your XP stats:", "Level information", false, embedBuilder).queue();
        } else {
            sendMessage(channel, Type.WARNING, "The Bot needs to have permission to manage custom emotes in order to display your xp stats!").queue();
        }
    }

    @Override
    public String info(Member member) {
        String prefix = settings.getPrefix(member.getGuild().getIdLong());
        return format("**Description**: Gives you information about your level.\n\n**Usage**: `%s[xp|lvl|level]`\n\n**Permission level**: `1`", prefix);
    }

    @Override
    public void onGenericGuildMessage(GenericGuildMessageEvent origevent) {
        if (origevent instanceof GuildMessageDeleteEvent || !bot.getCaching().getGuilds().get(origevent.getGuild().getIdLong()).isXp_system_activated())
            return;
        if (origevent instanceof GuildMessageReactionAddEvent) {
            GuildMessageReactionAddEvent event = (GuildMessageReactionAddEvent) origevent;
            event.getChannel().getMessageById(event.getMessageIdLong()).queue((msg) -> {
                if (!msg.getAuthor().isBot()) {
                    Discord_member discord_member = bot.getCaching().getMember().get(msg.getGuild().getIdLong()+" "+msg.getMember().getUser().getIdLong());
                    Discord_user discord_user = bot.getCaching().getUser().get(msg.getMember().getUser().getIdLong());
                    discord_member.setMember_xp(discord_member.getMember_xp()+1);
                    discord_user.setUser_xp(discord_user.getUser_xp()+1);
                }
            }, (msg) -> {});
        } else if (origevent instanceof GuildMessageReactionRemoveEvent) {
            GuildMessageReactionRemoveEvent event = (GuildMessageReactionRemoveEvent) origevent;
            event.getChannel().getMessageById(event.getMessageIdLong()).queue((msg) -> {
                if (!msg.getAuthor().isBot()) {
                    Discord_member discord_member = bot.getCaching().getMember().get(msg.getGuild().getIdLong()+" "+msg.getMember().getUser().getIdLong());
                    Discord_user discord_user = bot.getCaching().getUser().get(msg.getMember().getUser().getIdLong());
                    discord_member.setMember_xp(discord_member.getMember_xp()-1);
                    discord_user.setUser_xp(discord_user.getUser_xp()-1);
                }
            }, (msg) -> {});
        } else if (origevent instanceof GuildMessageReceivedEvent) {
            GuildMessageReceivedEvent event = (GuildMessageReceivedEvent) origevent;
            if (!event.getAuthor().isBot()) {
                if (event.getMessage().getType() != MessageType.DEFAULT || event.getAuthor().isBot())
                    return;

                int length = event.getMessage().getContentRaw().length();
                int result;
                if (length > 0) {
                    if (length > 10)
                        result = ThreadLocalRandom.current().nextInt(length - 10) + 10;
                    else
                        result = ThreadLocalRandom.current().nextInt(length);

                    Discord_member discord_member = bot.getCaching().getMember().get(event.getMember().getUser().getIdLong()+" "+event.getGuild().getIdLong());
                    Discord_user discord_user = bot.getCaching().getUser().get(event.getMember().getUser().getIdLong());
                    discord_member.setMember_xp(discord_member.getMember_xp()+result);
                    discord_user.setUser_xp(discord_user.getUser_xp()+result);
                }
            }
        }
        origevent.getChannel().getMessageById(origevent.getMessageId()).queue((msg) -> {
            if (msg.getType() == MessageType.DEFAULT && msg.getMember() != null)
                this.checkLvl(msg.getMember());
        }, (msg) -> {});
    }

    private String getProgressBar(long xp, long maxxp, Member member) {
        var jda = member.getJDA();
        Emote[] emotes = new Emote[8];
        emotes[0] = jda.getEmotesByName("empty1", true).get(0);
        emotes[1] = jda.getEmotesByName("empty2", true).get(0);
        emotes[2] = jda.getEmotesByName("empty2", true).get(0);
        emotes[3] = jda.getEmotesByName("empty2", true).get(0);
        emotes[4] = jda.getEmotesByName("empty2", true).get(0);
        emotes[5] = jda.getEmotesByName("empty2", true).get(0);
        emotes[6] = jda.getEmotesByName("empty2", true).get(0);
        emotes[7] = jda.getEmotesByName("empty3", true).get(0);
        if (maxxp/8<=xp) {
            emotes[0] = jda.getEmotesByName("full1", true).get(0);
            if (maxxp/8*2<=xp) {
                emotes[1] = jda.getEmotesByName("full2", true).get(0);
                if (maxxp / 8 * 3 <= xp) {
                    emotes[2] = jda.getEmotesByName("full2", true).get(0);
                    if (maxxp / 8 * 4 <= xp) {
                        emotes[3] = jda.getEmotesByName("full2", true).get(0);
                        if (maxxp / 8 * 5 <= xp) {
                            emotes[4] = jda.getEmotesByName("full2", true).get(0);
                            if (maxxp / 8 * 6 <= xp) {
                                emotes[5] = jda.getEmotesByName("full2", true).get(0);
                                if (maxxp / 8 * 7 <= xp) {
                                    emotes[6] = jda.getEmotesByName("full2", true).get(0);
                                    if (maxxp == xp) {
                                        emotes[7] = jda.getEmotesByName("full3", true).get(0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(emotes).map(Emote::getAsMention).forEach(stringBuilder::append);
        stringBuilder.append("\n\n");
        return stringBuilder.toString();
    }

    private void checkLvl(Member member) {
        Discord_member discord_member = bot.getCaching().getMember().get(member.getUser().getIdLong()+" "+member.getGuild().getIdLong());
        Discord_user discord_user = bot.getCaching().getUser().get(member.getUser().getIdLong());
        if (discord_user.getUser_xp()>=(discord_user.getUser_lvl()*multiplicator)) {
            discord_user.setUser_xp(0);
            discord_user.setUser_lvl(discord_user.getUser_lvl()+1);
        }
        if (discord_member.getMember_xp()>=(discord_member.getMember_lvl()*multiplicator)) {
            discord_member.setMember_xp(0);
            discord_member.setMember_lvl(discord_member.getMember_lvl()+1);
        }
    }
}
