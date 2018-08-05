package de.unhandledexceptions.codersclash.bot.commands;

import com.github.johnnyjayjay.discord.commandapi.CommandEvent;
import com.github.johnnyjayjay.discord.commandapi.ICommand;
import de.unhandledexceptions.codersclash.bot.core.Bot;
import de.unhandledexceptions.codersclash.bot.core.Config;
import de.unhandledexceptions.codersclash.bot.util.Messages;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;

import static de.unhandledexceptions.codersclash.bot.util.Messages.sendMessage;
import static de.unhandledexceptions.codersclash.bot.util.Messages.wrongUsageMessage;
import static java.lang.String.format;

/**
 * @author Hax
 * @time 14:34 25.07.2018
 * @project codersclashunhandledexceptionsbot
 * @package de.unhandledexceptions.codersclash.bot.commands
 * @class BotCommand
 **/

public class BotCommand implements ICommand {

    Config config;
    Bot bot;

    public BotCommand(Config config, Bot bot) {
        this.config = config;
        this.bot = bot;
    }

    @Override
    public void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {
        if (config.getBotOwners().contains(event.getAuthor().getIdLong())) {
            if (args.length>0) {
                switch (args[0].toLowerCase()) {
                    case "checkperms":
                    case "permscheck":
                        StringBuilder stringBuilder = new StringBuilder();
                        for (Permission permission: Permission.values()) {
                            stringBuilder.append(((event.getGuild().getSelfMember().hasPermission(permission)) ? "✅" : "❌") +" "+ permission.getName()+ "\n");
                        }
                        event.getChannel().sendMessage(
                                stringBuilder.toString()
                        ).queue();
                        break;

                    case "cache":
                        if (args.length==2) {
                            switch (args[1].toLowerCase()) {
                                case "read":
                                    sendMessage(channel, Messages.Type.WARNING, "Reading DB...", "Reading DB...", true).queue(
                                            msg -> {
                                                for (JDA jda : bot.getAPI().getShards()) {
                                                    msg.editMessage(new EmbedBuilder(msg.getEmbeds().get(0)).addField("Shard "+jda.getShardInfo().getShardId(), "Reading...", true).build()).queue();
                                                    msg = msg.getChannel().getMessageById(msg.getId()).complete();
                                                    bot.getCaching().clearfromjda(jda).readall(jda);
                                                    ArrayList<MessageEmbed.Field> fields = new ArrayList<>(msg.getEmbeds().get(0).getFields());
                                                    MessageEmbed.Field edit = fields.get(fields.size()-1);
                                                    EmbedBuilder builder =new EmbedBuilder(msg.getEmbeds().get(0)).clearFields();
                                                    for (MessageEmbed.Field field: fields) {
                                                        if (field != edit) {
                                                            builder.addField(field);
                                                        } else {
                                                            builder.addField(edit.getName(), "Read!", true);
                                                        }
                                                    }
                                                    msg.editMessage(builder.build()).queue();
                                                    msg = msg.getChannel().getMessageById(msg.getId()).complete();
                                                }
                                            }
                                    );

                                    break;
                                case "updatedb":
                                    sendMessage(channel, Messages.Type.WARNING, "Updating DB...", "caching", true).queue();
                                    bot.getCaching().updateDB();
                                    sendMessage(channel, Messages.Type.SUCCESS, "Updated DB!", "caching", true).queue();
                                    break;
                            }
                        } else wrongUsageMessage(channel, member, this);
                        break;
                    case "test":
                        sendMessage(Messages.getBundle("de"), channel, Messages.Type.SUCCESS,"util.test", "util.test").queue();
                        break;
                }

            } else Messages.wrongUsageMessage(channel, member, this);
        }
    }

    @Override
    public String info(Member member) {
        String prefix = Bot.getPrefix(member.getGuild().getIdLong());
        return format("**Usage**: `%s[bot|owner] cache [read|updatedb]`\n**Usage**: `%s[bot|owner] [checkperms|permscheck]`\n\n**Bot Owners only**", prefix, prefix);
    }
}
