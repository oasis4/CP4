package com.github.oasis.craftprotect.link;

public class MinecraftClientInfo {

    private final String clientId;

    private final String callbackURI;

    public MinecraftClientInfo(String clientId, String callbackURI) {
        this.clientId = clientId;
        this.callbackURI = callbackURI;
    }

    public String getClientId() {
        return clientId;
    }

    public String getCallbackURI() {
        return callbackURI;
    }
}
