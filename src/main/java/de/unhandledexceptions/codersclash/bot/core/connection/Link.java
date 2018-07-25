package de.unhandledexceptions.codersclash.bot.core.connection;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Collection;

public interface Link {

    void sendTyping(TextChannel channel);

    void distributeMessage(Message message);

    Collection<Long> getGuilds();

    long getLinkedChannel(Guild guild);

    void addChannel(TextChannel channel);

    boolean remove(Guild guild);

}