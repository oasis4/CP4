package com.github.oasis.craftprotect.link;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinecraftClientInfo {

    private String clientId;

    private String callbackURI;

}
