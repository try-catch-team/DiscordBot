package de.unhandledexceptions.codersclash.bot.listeners;

import de.unhandledexceptions.codersclash.bot.core.Bot;
import de.unhandledexceptions.codersclash.bot.core.Config;
import de.unhandledexceptions.codersclash.bot.util.Messages;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MentionListener extends ListenerAdapter {

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd LLL yyyy kk:mm:ss O", Locale.ENGLISH).withZone(ZoneId.of("Europe/Paris"));
    private Config config;
    private Bot bot;
    private final List<Object> CREATORS = Arrays.asList(261083609148948488L, 234343108773412864L, 226011931935375360L, 138607604506165248L);

    public MentionListener(Config config, Bot bot) {
        this.config = config;
        this.bot = bot;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equals(event.getGuild().getSelfMember().getAsMention())) {
            String prefix = Bot.getPrefix(event.getGuild().getIdLong());
            var shardManager = event.getJDA().asBot().getShardManager();
            var stringBuilder = new StringBuilder();
            long members = 0;
            for (Guild guild : shardManager.getGuildCache())
                members += guild.getMemberCache().size();

            CREATORS.forEach((id) -> stringBuilder.append(String.format("`%#s` ", shardManager.getUserById((long) id))));
            var builder = new EmbedBuilder();
            builder.clear().setThumbnail("https://i.imgur.com/L1RgtJb.gif")
                    .addField("Name", config.getBotName(), true)
                    .addField("Version", config.getVersion(), true)
                    .addField("Default Prefix", "`" + config.getPrefix() + "`", true)
                    .addField("This Guild's Prefix", "`" + prefix + "`", true)
                    .addField("Help Command", "`" + prefix + "[help|helpme|commands] <command>`", false)
                    .addField("Birth", "Fri, 22 Jun 2018 12:00:00 GMT+2", true)
                    .addField("Joined this Server", event.getGuild().getSelfMember().getJoinDate().format(dateTimeFormatter), true)
                    .addField("Creators", stringBuilder.toString(), false)
                    .addField("Shards", Long.toString(shardManager.getShards().size()), true)
                    .addField("Channels", Long.toString(shardManager.getTextChannelCache().size() + shardManager.getVoiceChannelCache().size()), true)
                    .addField("Servers", Long.toString(shardManager.getGuildCache().size()), true)
                    .addField("Members", Long.toString(members), true)
                    .addField("Current Uptime", this.getUptime(), true)
                    .addBlankField(true)
                    .addField("Source Code", "[GitHub](https://github.com/try-catch-team/DiscordBot)", true)
                    .addField("Help translating me!", "[Crowdin](https://crowdin.com/project/try-catch-bot)", true)
                    .addField("Need Help?", "[Join our Developement Server!](https://discord.gg/fKtyBF7)", true)
                    .addField("You want to use " + config.getBotName() + " on your Server too?", "[Invite him!](" + event.getJDA().asBot().getInviteUrl(Permission.ADMINISTRATOR) + ")", false)
                    .setColor(event.getGuild().getSelfMember().getColor());
            if (!bot.getCommandSettings().isActivated()) {
                builder.addField("CommandSettings are", event.getJDA().asBot().getShardManager().getEmotesByName("deactivated", false).get(0).getAsMention(), true);
            }
            Messages.sendMessage(event.getChannel(), Messages.Type.NO_TYPE, "Introducing... me! " + event.getJDA().asBot().getShardManager().getEmotesByName("partyparrot", false).get(0).getAsMention(), "Hi!", false, builder).queue();
        }
    }

    private String getUptime() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        long uptimeLong = runtime.getUptime();
        long second = (uptimeLong / 1000) % 60;
        long minute = (uptimeLong / (1000 * 60)) % 60;
        long hour = (uptimeLong / (1000 * 60 * 60)) % 24;
        long day = (uptimeLong / (1000 * 60 * 60 * 24));
        return String.format("**%d** days **%02d** hours **%02d** minutes **%02d** seconds", day, hour, minute, second);
    }
}
