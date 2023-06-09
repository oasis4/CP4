package com.github.oasis.craftprotect.model;

import com.github.oasis.craftprotect.api.GroupType;
import lombok.Data;

@Data
public class PlayerDisplayModel {

    private boolean awayFromKeyboard;

    private boolean live;

    private GroupType groupType = GroupType.NEW;

    public String toTabList(String name) {
        return (awayFromKeyboard ? "[AFK] " : "") + (live ? "[LIVE] " : "") + (groupType != null ? groupType.getPrefix() : "") + name;
    }

}
