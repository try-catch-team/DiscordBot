package de.unhandledexceptions.codersclash.bot.listeners;

import de.unhandledexceptions.codersclash.bot.core.Bot;
import de.unhandledexceptions.codersclash.bot.core.Config;
import de.unhandledexceptions.codersclash.bot.core.Database;
import de.unhandledexceptions.codersclash.bot.util.Logging;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.entities.SelfUser;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author TheRealYann
 */

public class ReadyListener extends ListenerAdapter {

    private Config config;
    private URL iconURL;
    private Database database;
    private Bot bot;

    private final Logger logger = Logging.getLogger();

    public ReadyListener(Config config, Database database, Bot bot) {
        this.config = config;
        try {
            this.iconURL = new URL(config.getIconURL());
        } catch (MalformedURLException e) {
            logger.error("MalformedURLException occurred:", e);
        }
        this.database = database;
        this.bot = bot;
    }

    @Override
    public void onReady(ReadyEvent event) {
        JDA jda = event.getJDA();
        SelfUser selfUser = jda.getSelfUser();
        try {
            if (selfUser.getAvatarUrl() == null) {
                Icon icon = Icon.from((iconURL).openStream());
                selfUser.getManager().setAvatar(icon).queue();
            }
        } catch (IOException e) {
            logger.error("An IOException occurred while creating icon/changing avatar", e);
        }

        if (!selfUser.getName().equals(config.getBotName())) {
            selfUser.getManager().setName(config.getBotName()).queue();
        }

        String guildName = config.getEmoteGuildName();
        if (jda.getGuildsByName(guildName, false).isEmpty()) {
            jda.addEventListener(new EmoteGuildListener(guildName));
            jda.createGuild(guildName).queue();
        }

        bot.getCaching().readall();
    }
}