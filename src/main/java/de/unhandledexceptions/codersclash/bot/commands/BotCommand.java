package de.unhandledexceptions.codersclash.bot.commands;

import com.github.johnnyjayjay.discord.commandapi.CommandEvent;
import com.github.johnnyjayjay.discord.commandapi.ICommand;
import de.unhandledexceptions.codersclash.bot.core.Bot;
import de.unhandledexceptions.codersclash.bot.core.Config;
import de.unhandledexceptions.codersclash.bot.util.Messages;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import static de.unhandledexceptions.codersclash.bot.util.Messages.sendMessage;
import static de.unhandledexceptions.codersclash.bot.util.Messages.wrongUsageMessage;

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
                                    sendMessage(channel, Messages.Type.WARNING, "Reading DB...", "caching", true).queue();
                                    bot.getCaching().readall();
                                    sendMessage(channel, Messages.Type.SUCCESS, "Read DB!", "caching", true).queue();
                                    break;
                                case "updatedb":
                                    sendMessage(channel, Messages.Type.WARNING, "Updating DB...", "caching", true).queue();
                                    bot.getCaching().updateDB();
                                    sendMessage(channel, Messages.Type.SUCCESS, "Updated DB!", "caching", true).queue();
                                    break;
                            }
                        } else wrongUsageMessage(channel, member, this);
                        break;
                }

            } else Messages.wrongUsageMessage(channel, member, this);
        }
    }

    @Override
    public String info(Member member) {
        return "**Usage**: `[Prefix][bot|owner] cache [read|updatedb]`\n**Usage**: `[Prefix][bot|owner] [checkperms|permscheck]`\n\n**BOT OWNERS ONLY**`";
    }
}
