package com.github.oasis.craftprotect.link;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwitchClientInfo {

    private String clientId;
    private String clientSecret;
    private String callbackURI;

    public String formattedURI() {
        return "https://id.twitch.tv/oauth2/authorize?client_id=%s&redirect_uri=%s&response_type=%s&scope=%s&state={sessionId}".formatted(clientId, callbackURI, "code", "");
    }

}
