package de.unhandledexceptions.codersclash.bot.core.caching;

/**
 * @author Hax
 * @time 11:55 23.07.2018
 * @project codersclashunhandledexceptionsbot
 * @package de.unhandledexceptions.codersclash.bot.core.caching
 * @class Discord_member
 **/

public class Discord_member {

    private long member_id;
    private long guild_id;
    private long user_id;
    private int member_xp;
    private int member_lvl;
    private int permission_lvl;

    public Discord_member(long member_id, long guild_id, long user_id, int member_xp, int member_lvl, int permission_lvl) {
        this.member_id = member_id;
        this.guild_id = guild_id;
        this.user_id = user_id;
        this.member_xp = member_xp;
        this.member_lvl = member_lvl;
        this.permission_lvl = permission_lvl;
    }

    public long getMember_id() {
        return member_id;
    }

    public void setMember_id(long member_id) {
        this.member_id = member_id;
    }

    public long getGuild_id() {
        return guild_id;
    }

    public void setGuild_id(long guild_id) {
        this.guild_id = guild_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getMember_xp() {
        return member_xp;
    }

    public void setMember_xp(int member_xp) {
        this.member_xp = member_xp;
    }

    public int getMember_lvl() {
        return member_lvl;
    }

    public void setMember_lvl(int member_lvl) {
        this.member_lvl = member_lvl;
    }

    public int getPermission_lvl() {
        return permission_lvl;
    }

    public void setPermission_lvl(int permission_lvl) {
        this.permission_lvl = permission_lvl;
    }
}
