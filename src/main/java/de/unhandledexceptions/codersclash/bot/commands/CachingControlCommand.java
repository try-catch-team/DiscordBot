package de.unhandledexceptions.codersclash.bot.commands;

import com.github.johnnyjayjay.discord.commandapi.CommandEvent;
import com.github.johnnyjayjay.discord.commandapi.ICommand;
import de.unhandledexceptions.codersclash.bot.core.Bot;
import de.unhandledexceptions.codersclash.bot.core.caching.Caching;
import de.unhandledexceptions.codersclash.bot.core.caching.Discord_member;
import de.unhandledexceptions.codersclash.bot.util.Messages;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import static de.unhandledexceptions.codersclash.bot.util.Messages.noPermissionsMessage;
import static de.unhandledexceptions.codersclash.bot.util.Messages.sendMessage;
import static de.unhandledexceptions.codersclash.bot.util.Messages.wrongUsageMessage;

/**
 * @author Hax
 * @time 12:41 23.07.2018
 * @project codersclashunhandledexceptionsbot
 * @package de.unhandledexceptions.codersclash.bot.commands
 * @class CachingControlCommand
 **/

public class CachingControlCommand implements ICommand {

    private Caching caching;

    public CachingControlCommand(Caching caching) {
        this.caching = caching;
    }

    @Override
    public void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {
        if (!Bot.getBotOwners().contains(member.getUser().getIdLong()))
            return;

        Discord_member memberObject = caching.getMember().get(member.getUser().getIdLong()+ " "+ member.getGuild().getIdLong());
        if (memberObject.getPermission_lvl()>=5) {
            if (args.length>0) {
                if (args.length==1) {
                    switch (args[0].toLowerCase()) {
                        case "read":
                            sendMessage(channel, Messages.Type.WARNING, "Reading DB...", "caching", true).queue();
                            caching.readall();
                            sendMessage(channel, Messages.Type.SUCCESS, "Readed DB!", "caching", true).queue();
                            break;
                        case "updatedb":
                            sendMessage(channel, Messages.Type.WARNING, "Updating DB...", "caching", true).queue();
                            caching.updateDB();
                            sendMessage(channel, Messages.Type.SUCCESS, "Updated DB!", "caching", true).queue();
                            break;
                    }
                }
            } else wrongUsageMessage(channel, member, this);
        } else noPermissionsMessage(channel, member);
    }

    @Override
    public String info(Member member) {
        return "For bot owners only!";
    }

}
