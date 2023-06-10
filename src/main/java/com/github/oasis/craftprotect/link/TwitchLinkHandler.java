package com.github.oasis.craftprotect.link;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.feature.LiveStreamFeature;
import com.github.oasis.craftprotect.utils.UrlUtils;
import com.github.twitch4j.helix.domain.StreamList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.netflix.hystrix.HystrixCommand;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class TwitchLinkHandler implements HttpHandler {

    private final CraftProtectPlugin plugin;
    private final TwitchClientInfo info;
    private final LiveStreamFeature feature;

    public TwitchLinkHandler(CraftProtectPlugin plugin, TwitchClientInfo info, LiveStreamFeature feature) {
        this.plugin = plugin;
        this.info = info;
        this.feature = feature;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if (!"GET".equals(exchange.getRequestMethod()))
                return;

            System.out.println(exchange.getRequestURI());

            String query = exchange.getRequestURI().getQuery();
            Map<String, String> paramMap = UrlUtils.getParamMap(query);

            System.out.println(paramMap.get("code"));
            System.out.println(paramMap.get("state"));

            Execution execution = plugin.getAuthorizationCache().getIfPresent(paramMap.get("state"));
            if (!(execution instanceof TwitchExecution twitchExecution)) {
                return;
            }


            HttpURLConnection connection = (HttpURLConnection) new URL("https://id.twitch.tv/oauth2/token").openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.getOutputStream().write("client_id=%s&client_secret=%s&code=%s&grant_type=authorization_code&redirect_uri=%s".formatted(info.getClientId(), info.getClientSecret(), paramMap.get("code"), info.getCallbackURI()).getBytes());
            connection.connect();

            System.out.println(connection.getResponseCode());

            InputStream inputStream = connection.getInputStream();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(new InputStreamReader(inputStream), JsonObject.class);
            String accessToken = jsonObject.get("access_token").getAsString();


            connection = (HttpURLConnection) new URL("https://id.twitch.tv/oauth2/validate").openConnection();
            connection.setRequestProperty("Authorization", "OAuth %s".formatted(accessToken));
            connection.connect();

            JsonObject jsonObject1 = gson.fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);
            String userId = jsonObject1.get("user_id").getAsString();
            System.out.println(userId);
            System.out.println(jsonObject1.get("login").getAsString());

            System.out.println("Revoking...");
            connection = (HttpURLConnection) new URL("https://id.twitch.tv/oauth2/revoke").openConnection();
            connection.setDoOutput(true);
            connection.getOutputStream().write("client_id=%s&token=%s".formatted(info.getClientId(), accessToken).getBytes());
            connection.connect();

            System.out.println(connection.getResponseCode());

            HystrixCommand<StreamList> streams = feature.getTwitchClient().getHelix().getStreams(null, null, null, "live", null, null, null, List.of(userId), null);
            StreamList execute = streams.execute();

            boolean live = execute.getStreams().stream().anyMatch(stream -> "live".equals(stream.getType()));
            System.out.println("Live: " + live);

            twitchExecution.execute(userId, live);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
