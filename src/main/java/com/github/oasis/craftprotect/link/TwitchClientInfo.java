package com.github.oasis.craftprotect.link;

public class TwitchClientInfo {

    private final String clientId;
    private final transient String clientSecret;

    private final String callbackURI;

    public TwitchClientInfo(String clientId, String clientSecret, String callbackURI) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.callbackURI = callbackURI;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getCallbackURI() {
        return callbackURI;
    }
}
