package de.unhandledexceptions.codersclash.bot.commands;

import com.github.johnnyjayjay.discord.commandapi.CommandEvent;
import com.github.johnnyjayjay.discord.commandapi.CommandSettings;
import com.github.johnnyjayjay.discord.commandapi.ICommand;
import de.unhandledexceptions.codersclash.bot.core.Bot;
import de.unhandledexceptions.codersclash.bot.core.Database;
import de.unhandledexceptions.codersclash.bot.core.Permissions;
import de.unhandledexceptions.codersclash.bot.core.caching.Discord_member;
import de.unhandledexceptions.codersclash.bot.core.caching.Discord_user;
import de.unhandledexceptions.codersclash.bot.util.Messages;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.*;

import static de.unhandledexceptions.codersclash.bot.util.Messages.noPermissionsMessage;
import static de.unhandledexceptions.codersclash.bot.util.Messages.sendMessage;
import static java.lang.String.format;

public class ScoreBoardCommand implements ICommand {

    private CommandSettings settings;
    private Bot bot;

    public ScoreBoardCommand(Bot bot, CommandSettings settings) {
        this.bot = bot;
        this.settings = settings;
    }
    @Override
    public void onCommand(CommandEvent commandEvent, Member member, TextChannel textChannel, String[] strings) {
        if (!commandEvent.getGuild().getSelfMember().hasPermission(textChannel, Permission.MESSAGE_WRITE))
            return;
        if (Permissions.getPermissionLevel(member) >= 1) {
            Comparator<Discord_member> members = new Comparator<>() {
                @Override
                public int compare(Discord_member o1, Discord_member o2) {
                    return o1.getMember_lvl()-o2.getMember_lvl();
                }
            };
            Comparator<Discord_user> users = new Comparator<>() {
                @Override
                public int compare(Discord_user o1, Discord_user o2) {
                    return o1.getUser_lvl()-o2.getUser_lvl();
                }
            };
            List<Discord_member> discord_members = new ArrayList<>(bot.getCaching().getMember().values());
            Collections.sort(discord_members, members);
            List<Discord_user> discord_users = new ArrayList<>(bot.getCaching().getUser().values());
            Collections.sort(discord_users, users);


            for (int i =0; discord_members.size()-1>i; i++) {
                if (!(discord_members.get(i).getMember_lvl()>discord_members.get(i+1).getMember_lvl())) {
                    if (!(discord_members.get(i).getMember_xp()>discord_members.get(i+1).getMember_xp())) {
                        Discord_member o1 = discord_members.get(i);
                        Discord_member o2 = discord_members.get(i+1);
                        ArrayList<Discord_member> discord_members1 = new ArrayList<>()  {{
                            add(o1);
                            add(o2);
                        }};
                        Collections.sort(discord_members1, members);
                    }
                }
            }
            for (int i =0; discord_members.size()-1>i; i++) {
                if (!(discord_members.get(i).getMember_lvl()>discord_members.get(i+1).getMember_lvl())) {
                    if (!(discord_members.get(i).getMember_xp()>discord_members.get(i+1).getMember_xp())) {
                        Discord_member o1 = discord_members.get(i);
                        Discord_member o2 = discord_members.get(i+1);
                        ArrayList<Discord_member> discord_members1 = new ArrayList<>()  {{
                            add(o1);
                            add(o2);
                        }};
                        Collections.sort(discord_users, users);
                    }
                }
            }
            StringBuilder builder = new StringBuilder();
            builder.append("```");
            for (int i =discord_members.size()-1; i!=0 && discord_members.size()-10<i; i--) {
                builder.append(bot.getAPI().getUserById(discord_members.get(i).getUser_id()).getName() + "#" +
                        bot.getAPI().getUserById(discord_members.get(i).getUser_id()).getDiscriminator() + "   " +
                        "\tLevel: " + discord_members.get(i).getMember_lvl() + "   " + "\tXP: " + discord_members.get(i).getMember_xp() + "\n");
            }
            builder.append("```");
            // thanks for following iglookid <3
            int place1 =0;
            for (int i =discord_members.size()-1; i!=0; i--) {
                Discord_member member1 = discord_members.get(i);
                if (commandEvent.getMember().getUser().getIdLong() == member1.getUser_id() && commandEvent.getMember().getGuild().getIdLong() == member1.getGuild_id()) {
                    place1 = place1+1;
                    break;
                }
                place1++;
            }
            builder.append(":arrow_right: **Your place**\n"+place1);
            builder.append("```");
            for (int i =discord_users.size()-1; i!=0 && discord_users.size()-10<i; i--) {
                builder.append(bot.getAPI().getUserById(discord_users.get(i).getUser_id()).getName() + "#" +
                        bot.getAPI().getUserById(discord_users.get(i).getUser_id()).getDiscriminator() + "   " +
                        "\tLevel: " + discord_users.get(i).getUser_lvl() + "   " + "\tXP: " + discord_users.get(i).getUser_xp() + "\n");
            }
            int place2 =0;
            for (int i =discord_users.size()-1; i!=0; i--) {
                Discord_user user1 = discord_users.get(i);
                if (commandEvent.getMember().getUser().getIdLong() == user1.getUser_id()) {
                    place2 = place2+1;
                    break;
                }
                place2++;
            }
            builder.append("```");
            builder.append(":arrow_right: **Your place**\n"+place2);
            sendMessage(textChannel, Messages.Type.INFO, "for `" + commandEvent.getGuild().getName() + "`", "Scoreboard", false,
                    new EmbedBuilder().addField("ScoreBoard", builder.toString(), false)).queue();
        } else {
            noPermissionsMessage(textChannel, member);
        }
    }
    @Override
    public String info(Member member) {
        return format("**Description**: Gives you information about your score and the best scores.\n\n**Usage**: `%s[scoreboard|sb]`\n\n**Permission level**: `1`",
                settings.getPrefix(member.getGuild().getIdLong()));
    }
}
