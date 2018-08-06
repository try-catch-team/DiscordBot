package de.unhandledexceptions.codersclash.bot.listeners;

import de.unhandledexceptions.codersclash.bot.core.Bot;
import de.unhandledexceptions.codersclash.bot.core.Config;
import de.unhandledexceptions.codersclash.bot.util.Messages;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static de.unhandledexceptions.codersclash.bot.util.Messages.sendMessage;
import static java.lang.String.format;

/**
 * @author Hax
 * @time 14:34 25.07.2018
 * @project codersclashunhandledexceptionsbot
 * @package de.unhandledexceptions.codersclash.bot.listeners
 * @class BotTools
 **/

public class BotTools extends ListenerAdapter {

    Config config;
    Bot bot;

    public BotTools(Config config, Bot bot) {
        this.config = config;
        this.bot = bot;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String prefix = Bot.getPrefix(event.getGuild().getIdLong());
        String regex = prefix + "(?i)bot cache ((read)|(updatedb))";
        String regexWrongUsage = prefix + "(?i)bot.*";
        if (Bot.getBotOwners().contains(event.getAuthor().getIdLong())) {
            var argsList = Arrays.asList(event.getMessage().getContentRaw().replaceFirst(Bot.getPrefix(event.getGuild().getIdLong()), "")
                    .split("\\s+"));
            String[] args = argsList.subList(1, argsList.size()).toArray(new String[argsList.size()]);
            if (event.getMessage().getContentRaw().matches(regex)) {
                var channel = event.getChannel();
                if (args[1].equalsIgnoreCase("read")) {
                    sendMessage(channel, Messages.Type.WARNING, "Reading DB...", "Reading DB...", true).queue(
                            msg -> {
                                for (JDA jda : bot.getAPI().getShards()) {
                                    msg.editMessage(new EmbedBuilder(msg.getEmbeds().get(0)).addField("Shard " + jda.getShardInfo().getShardId(), "Reading...", true).build()).queue();
                                    msg = msg.getChannel().getMessageById(msg.getId()).complete();
                                    bot.getCaching().clearfromjda(jda).readall(jda);
                                    ArrayList<MessageEmbed.Field> fields = new ArrayList<>(msg.getEmbeds().get(0).getFields());
                                    MessageEmbed.Field edit = fields.get(fields.size()-1);
                                    EmbedBuilder builder =new EmbedBuilder(msg.getEmbeds().get(0)).clearFields();
                                    for (MessageEmbed.Field field: fields) {
                                        if (field != edit) {
                                            builder.addField(field);
                                        } else {
                                            builder.addField(edit.getName(), "Done!", true)
                                                    .setFooter("Success", "https://cdn.pixabay.com/photo/2012/04/11/17/44/check-mark-29114_960_720.png")
                                                    .setColor(Color.GREEN)
                                                    .setTitle("Database has been read!");
                                        }
                                    }
                                    msg.editMessage(builder.build()).queue();
                                    msg = msg.getChannel().getMessageById(msg.getId()).complete();
                                }
                            }
                    );
                } else if (args[1].equalsIgnoreCase("updatedb")) {
                    sendMessage(channel, Messages.Type.WARNING, "Updating Database...", "Caching", true).queue();
                    bot.getCaching().updateDB();
                    sendMessage(channel, Messages.Type.SUCCESS, "Database successfully updated!", "Caching", true).queue();
                }
            } else if (event.getMessage().getContentRaw().matches(prefix + "(?i)bot ((perms)|(checkperms)|(permscheck))")) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Permission permission: Permission.values()) {
                    stringBuilder.append(((event.getGuild().getSelfMember().hasPermission(permission)) ? "✅" : "❌") +" "+ permission.getName()+ "\n");
                }
                event.getChannel().sendMessage(
                        stringBuilder.toString()
                ).queue();
            } else if (event.getMessage().getContentRaw().matches(regexWrongUsage))
                sendMessage(event.getChannel(), Messages.Type.WARNING,
                        format("**Usage**: `%sbot cache [read|updatedb]`\n\t\t\t  `%sbot [perms|checkperms|permscheck]`\n\n**Bot Owners only.**", prefix, prefix)).queue();
        } else if (event.getMessage().getContentRaw().matches(regex) || event.getMessage().getContentRaw().matches(regexWrongUsage))
            sendMessage(event.getChannel(), Messages.Type.ERROR, format("Nothing to see here. **Bot Owners only.** %s", event.getMember().getAsMention())).queue((msg) -> msg.delete().queueAfter(7, TimeUnit.SECONDS));
    }
}