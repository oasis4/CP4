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

}
