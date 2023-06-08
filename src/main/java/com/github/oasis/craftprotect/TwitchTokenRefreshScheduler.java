package com.github.oasis.craftprotect;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class TwitchTokenRefreshScheduler implements Closeable {

    private static final String REQUEST_URL = "https://id.twitch.tv/oauth2/token";

    private final String clientId, clientSecret;

    private String accessToken;

    private final Thread refresher;

    public TwitchTokenRefreshScheduler(Logger logger, String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        this.refresher = new Thread(() -> {

            try {
                do {
                    int expiresIn = refreshToken();
                    System.out.println("Refreshing app token in " + expiresIn + " seconds...");
                    Thread.sleep((expiresIn - 30) * 1000L);
                } while (true);
            } catch (IOException e) {
                logger.severe("Failed to provide new access token");
                throw new RuntimeException(e);
            } catch (InterruptedException ignored) {
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }, "Twitch-Token-Refresher");
        this.refresher.start();

    }

    private int refreshToken() throws IOException, IllegalAccessException {
        URL url = new URL(REQUEST_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.connect();

        try (OutputStream stream = connection.getOutputStream()) {
            stream.write("client_id=%s&client_secret=%s&grant_type=client_credentials".formatted(clientId, clientSecret).getBytes());
            stream.flush();
        }
        int expiresIn;
        try (InputStream stream = connection.getInputStream()) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(new InputStreamReader(stream), JsonObject.class);
            if (jsonObject == null)
                throw new IllegalStateException("Cannot parse response");

            if(jsonObject.has("status")) {
                int status = jsonObject.get("status").getAsInt();
                String message = jsonObject.get("message").getAsString();
                throw new IllegalAccessException(status + ": " + message);
            }

            accessToken = jsonObject.get("access_token").getAsString();
            expiresIn = jsonObject.get("expires_in").getAsInt();
            if (!jsonObject.get("token_type").getAsString().equals("bearer"))
                throw new IllegalStateException("Got another type of token instead of bearer");
        }

        return expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public void close() {
        this.refresher.interrupt();
    }
}
