package de.unhandledexceptions.codersclash.bot.entities;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Hax
 * @time 13:23 06.08.2018
 * @project codersclashunhandledexceptionsbot
 * @package de.unhandledexceptions.codersclash.bot.entities
 * @class AutoChannel
 **/

public class AutoChannel {
    private long ownerId;
    private Set<Long> users;
    private long voiceChannelId;
    private long textChannelId;
    private JDA jda;

    public AutoChannel(long voiceChannelId, long textChannelId, long ownerId, JDA jda) {
        this.voiceChannelId = voiceChannelId;
        this.textChannelId = textChannelId;
        this.ownerId = ownerId;
        this.jda = jda;
        this.users = new HashSet<>();
        users.add(ownerId);
    }

    public void addUser(User user) {
        users.add(user.getIdLong());
    }

    public boolean removeUser(User user) {
        users.remove(user.getIdLong());
        return users.isEmpty();
    }

    public long getOwnerId() {
        return ownerId;
    }

    public Set<Long> getUsers() {
        return users;
    }

    public long getVoiceChannelId() {
        return voiceChannelId;
    }

    public long getTextChannelId() {
        return textChannelId;
    }

    public JDA getJda() {
        return jda;
    }

    public VoiceChannel getVoiceChannel() {
        return jda.getVoiceChannelById(voiceChannelId);
    }

    public TextChannel getTextChannel() {
        return jda.getTextChannelById(textChannelId);
    }
}
