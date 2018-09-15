package de.unhandledexceptions.codersclash.bot.core.caching;

import de.unhandledexceptions.codersclash.bot.core.Bot;
import de.unhandledexceptions.codersclash.bot.core.Database;
import de.unhandledexceptions.codersclash.bot.util.Logging;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hax
 * @time 11:49 23.07.2018
 * @project codersclashunhandledexceptionsbot
 * @package de.unhandledexceptions.codersclash.bot.core
 * @class Chaching
 **/

public class Caching {

    private static Logger logger = Logging.getLogger();

    private Database database;

    private Map<Long, Discord_guild> guilds;
    private Map<String, Discord_member> members;
    private Map<Long, Discord_user> users;

    public Caching(Database database, Bot bot) {
        this.database = database;
        guilds = new HashMap<>();
        members = new HashMap<>();
        users = new HashMap<>();
    }

    public Caching clearall() {
        guilds.clear();
        members.clear();
        users.clear();
        return this;
    }

    public Caching clearfromjda(JDA jda) {
        for (Guild guild:jda.getGuilds()) {
            guilds.remove(guild.getIdLong());
            for (Member member: guild.getMembers()) {
                members.remove(member.getUser().getIdLong()+" "+member.getGuild().getIdLong());
            }
        }
        for (User user: jda.getUsers()) {
            users.remove(user.getIdLong());
        }
        return this;
    }

    public Caching readall(JDA jda) {
        logger.warn("Reading DB...");
        database.readall(this, jda);
        logger.info("DB has been read!");
        return this;
    }

    public Caching updateDB() {
        logger.warn("Updating DB...");
        database.updateDB(this);
        logger.info("DB has been updated!");
        return this;
    }

    public Map<Long, Discord_guild> getGuilds() {
        return guilds;
    }

    public Map<String, Discord_member> getMember() {
        return members;
    }

    public Map<Long, Discord_user> getUser() {
        return users;
    }

}
