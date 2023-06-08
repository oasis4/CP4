package com.github.oasis.craftprotect.utils;

import com.github.oasis.craftprotect.api.GroupType;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlayerDisplay {

    private List<Consumer<PlayerDisplay>> subscriber = new ArrayList<>();

    private boolean live;

    private GroupType groupType;

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
        subscriber.forEach(consumer -> consumer.accept(this));
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
        subscriber.forEach(consumer -> consumer.accept(this));
    }

    public Closeable subscribe(Consumer<PlayerDisplay> consumer) {
        this.subscriber.add(consumer);
        return () -> this.subscriber.remove(consumer);
    }

    public Closeable executeAndSubscribe(Consumer<PlayerDisplay> consumer) {
        consumer.accept(this);
        return subscribe(consumer);
    }

    public String toTabList(String name) {
        return (live ? "[LIVE] " : "") + (groupType != null ? groupType.getPrefix() : "") + name;
    }

}
