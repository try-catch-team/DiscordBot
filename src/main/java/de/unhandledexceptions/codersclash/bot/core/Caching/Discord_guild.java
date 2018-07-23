package de.unhandledexceptions.codersclash.bot.core.Caching;

/**
 * @author Hax
 * @time 11:51 23.07.2018
 * @project codersclashunhandledexceptionsbot
 * @package de.unhandledexceptions.codersclash.bot.core.Caching
 * @class Discord_guild
 **/

public class Discord_guild {

    int reports_until_ban;
    boolean xp_system_activated;
    String prefix;
    long guild_id;
    long mail_channel;
    long auto_channel;

    public Discord_guild(int reports_until_ban, boolean xp_system_activated, String prefix, long guild_id, long mail_channel, long auto_channel) {
        this.reports_until_ban = reports_until_ban;
        this.xp_system_activated = xp_system_activated;
        this.prefix = prefix;
        this.guild_id = guild_id;
        this.mail_channel = mail_channel;
        this.auto_channel = auto_channel;
    }

    public void setReports_until_ban(int reports_until_ban) {
        this.reports_until_ban = reports_until_ban;
    }

    public void setXp_system_activated(boolean xp_system_activated) {
        this.xp_system_activated = xp_system_activated;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setGuild_id(long guild_id) {
        this.guild_id = guild_id;
    }

    public void setMail_channel(long mail_channel) {
        this.mail_channel = mail_channel;
    }

    public void setAuto_channel(long auto_channel) {
        this.auto_channel = auto_channel;
    }

    public int getReports_until_ban() {
        return reports_until_ban;
    }

    public boolean isXp_system_activated() {
        return xp_system_activated;
    }

    public String getPrefix() {
        return prefix;
    }

    public long getGuild_id() {
        return guild_id;
    }

    public long getMail_channel() {
        return mail_channel;
    }

    public long getAuto_channel() {
        return auto_channel;
    }
}
