package com.github.oasis.craftprotect.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CraftProtectUser {

    private UUID id;

    private String twitchId;

    private String discordId;

}
