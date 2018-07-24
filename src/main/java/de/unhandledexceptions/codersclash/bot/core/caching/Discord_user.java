package de.unhandledexceptions.codersclash.bot.core.caching;

/**
 * @author Hax
 * @time 11:58 23.07.2018
 * @project codersclashunhandledexceptionsbot
 * @package de.unhandledexceptions.codersclash.bot.core.caching
 * @class Discord_user
 **/

public class Discord_user {

    private long user_id;
    private int user_xp;
    private int user_lvl;

    public Discord_user(long user_id, int user_xp, int user_lvl) {
        this.user_id = user_id;
        this.user_xp = user_xp;
        this.user_lvl = user_lvl;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getUser_xp() {
        return user_xp;
    }

    public void setUser_xp(int user_xp) {
        this.user_xp = user_xp;
    }

    public int getUser_lvl() {
        return user_lvl;
    }

    public void setUser_lvl(int user_lvl) {
        this.user_lvl = user_lvl;
    }
}
