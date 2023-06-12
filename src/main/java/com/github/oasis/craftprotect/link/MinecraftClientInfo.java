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

    public String formattedURI() {
        return "https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize?client_id=%s&response_type=token&redirect_uri=%s&scope=XboxLive.signin&response_mode=fragment&state={sessionId}&nonce=678910".formatted(clientId, callbackURI);
    }
}
