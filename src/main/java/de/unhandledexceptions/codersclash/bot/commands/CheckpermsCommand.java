package de.unhandledexceptions.codersclash.bot.commands;

import com.github.johnnyjayjay.discord.commandapi.CommandEvent;
import com.github.johnnyjayjay.discord.commandapi.ICommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * @author Hax
 * @time 11:53 24.07.2018
 * @project codersclashunhandledexceptionsbot
 * @package de.unhandledexceptions.codersclash.bot.commands
 * @class CheckpermsCommand
 **/

public class CheckpermsCommand implements ICommand {
    @Override
    public void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Permission permission: Permission.values()) {
            stringBuilder.append(((event.getGuild().getSelfMember().hasPermission(permission)) ? "✅" : "❌") +" "+ permission.getName()+ "\n");
        }
        event.getChannel().sendMessage(
            stringBuilder.toString()
        ).queue();
    }

    @Override
    public String info(Member member) {
        return null;
    }
}
