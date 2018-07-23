package de.unhandledexceptions.codersclash.bot.core.Caching;

import com.github.johnnyjayjay.discord.commandapi.CommandEvent;
import com.github.johnnyjayjay.discord.commandapi.ICommand;
import de.unhandledexceptions.codersclash.bot.core.Bot;
import de.unhandledexceptions.codersclash.bot.core.Database;
import de.unhandledexceptions.codersclash.bot.util.Logging;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;

import java.util.HashMap;

/**
 * @author Hax
 * @time 11:49 23.07.2018
 * @project codersclashunhandledexceptionsbot
 * @package de.unhandledexceptions.codersclash.bot.core
 * @class Chaching
 **/

public class Caching {

    private static Logger logger = Logging.getLogger();

    Database database;
    Bot bot;

    HashMap<Long, Discord_guild> guilds;
    HashMap<String, Discord_member> member;
    HashMap<Long, Discord_user> user;

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
        logger.info("Readed DB!");
        return this;
    }

    public HashMap<Long, Discord_guild> getGuilds() {
        return guilds;
    }

    public HashMap<String, Discord_member> getMember() {
        return member;
    }

    public HashMap<Long, Discord_user> getUser() {
        return user;
    }

}
