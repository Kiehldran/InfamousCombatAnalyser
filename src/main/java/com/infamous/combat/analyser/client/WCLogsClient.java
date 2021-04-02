package com.infamous.combat.analyser.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.infamous.combat.analyser.BasicModule;
import com.infamous.combat.analyser.client.dto.AccessTokenDTO;
import com.netflix.graphql.dgs.client.DefaultGraphQLClient;
import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.HttpResponse;
import okhttp3.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class WCLogsClient {

    private static final String AUTH_URI = "https://classic.warcraftlogs.com/oauth/authorize";
    private static final String TOKEN_URI = "https://classic.warcraftlogs.com/oauth/token";
    private static final String API_URI = "https://classic.warcraftlogs.com/api/v2/client";

    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";

    private static final int NAXXRAMAS_ZONE_ID = 1006;
    private static final String SERVER_GEHENNAS = "Gehennas";
    private static final String REGION_EUROPE = "eu";

    private final ObjectMapper objectMapper;

    private AccessTokenDTO accessToken;
    private long accessTokenObtainTime;
    private boolean isAccessTokenExpired = true;

    public WCLogsClient() {
        Injector injector = Guice.createInjector(new BasicModule());
        objectMapper = injector.getInstance(ObjectMapper.class);
    }

    public String getRecentReports() throws IOException {
        attemptToRefreshAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        DefaultGraphQLClient graphQLClient = new DefaultGraphQLClient(API_URI);

        String query = "{\n" +
                "  characterData {\n" +
                "    character(name: \"Kiehl\", serverSlug: \"Gehennas\", serverRegion: \"eu\") {\n" +
                "      recentReports(limit: 20) {\n" +
                "        reports: data {\n" +
                "          code\n" +
                "          startTime\n" +
                "          endTime\n" +
                "          zone {\n" +
                "            name\n" +
                "            id\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n";
        GraphQLResponse response = graphQLClient.executeQuery(query, new HashMap<>(),  (url, headers, body) -> {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("Authorization", "Bearer " + accessToken.getAccessToken());
            headers.forEach(requestHeaders::put);

            ResponseEntity<String> exchange = restTemplate.exchange(API_URI, HttpMethod.POST, new HttpEntity<>(body, requestHeaders), String.class);

            return new HttpResponse(exchange.getStatusCode().value(), exchange.getBody());
        });

        return response.toString();
    }

    private void attemptToRefreshAccessToken() throws IOException {
        updateAccessTokenExpiry();
        if(isAccessTokenExpired) {
            OkHttpClient httpClient = new OkHttpClient();

            Request accessTokenRequest = getAccessTokenRequest();

            try(Response response = httpClient.newCall(accessTokenRequest).execute()) {
                if(!response.isSuccessful()) {
                    throw new RuntimeException("Failed getting access token");
                }
                String responseString = response.body().string();
                accessToken = objectMapper.readValue(responseString, AccessTokenDTO.class);
                LOGGER.info("Access Token was expired and had been refreshed.");
                isAccessTokenExpired = false;
                accessTokenObtainTime = Instant.now().toEpochMilli();
            }
        }
    }

    private void updateAccessTokenExpiry() {
        if(accessToken == null) {
            isAccessTokenExpired = true;
        } else {
            long now = Instant.now().toEpochMilli();
            if((now - accessTokenObtainTime) >= accessToken.getExpiresIn())
                isAccessTokenExpired = true;
            else
                isAccessTokenExpired= false;
        }
    }

    private Request getAccessTokenRequest() {
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();

        String auth = CLIENT_ID + ":" + CLIENT_SECRET;

        byte[] encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.ISO_8859_1)).getBytes();
        String authHeader = "Basic " + new String(encodedAuth);

        return new Request.Builder()
                .url(TOKEN_URI)
                .addHeader(AUTHORIZATION, authHeader)
                .post(formBody)
                .build();
    }
}
