package de.unhandledexceptions.codersclash.bot.core.caching;

import de.unhandledexceptions.codersclash.bot.core.Bot;
import de.unhandledexceptions.codersclash.bot.core.Database;
import de.unhandledexceptions.codersclash.bot.util.Logging;
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
    private Bot bot;

    private Map<Long, Discord_guild> guilds;
    private Map<String, Discord_member> member;
    private Map<Long, Discord_user> user;

    public Caching(Database database, Bot bot) {
        this.database = database;
        this.bot = bot;
        guilds = new HashMap<>();
        member = new HashMap<>();
        user = new HashMap<>();
    }

    public Caching readall() {
        logger.warn("Reading DB...");
        database.readall(this, bot);
        logger.info("Readed DB!");
        return this;
    }

    public Caching updateDB() {
        logger.warn("Updating DB...");
        database.updateDB(this);
        logger.info("Updated DB!");
        return this;
    }

    public Map<Long, Discord_guild> getGuilds() {
        return guilds;
    }

    public Map<String, Discord_member> getMember() {
        return member;
    }

    public Map<Long, Discord_user> getUser() {
        return user;
    }

}
