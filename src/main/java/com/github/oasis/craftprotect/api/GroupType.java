package com.github.oasis.craftprotect.api;

public enum GroupType {

    GOLD(604800000, "§6GOLD§7・"),
    ACTIVE(86400000, "§eAktiv§7・"),
    NEWBIE(18000000, "§7Anfänger§7・"),
    NEW(0, "§7Neu§7・");


    private final long time;
    private final String prefix;

    GroupType(long time, String prefix) {
        this.time = time;
        this.prefix = prefix;
    }

    public long getTime() {
        return time;
    }

    public String getPrefix() {
        return prefix;
    }
}
