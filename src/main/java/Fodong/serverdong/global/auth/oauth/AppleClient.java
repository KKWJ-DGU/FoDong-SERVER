package Fodong.serverdong.global.auth.oauth;

import Fodong.serverdong.global.auth.dto.request.AppleTokenRequestBody;
import Fodong.serverdong.global.auth.dto.response.AppleTokenResponse;
import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AppleClient {

    @Value("${oauth.provider.apple.auth-url}")
    private String appleAuthUrl;

    public AppleTokenResponse appleTokenResponse(AppleTokenRequestBody requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        WebClient webClient = WebClient.builder()
                .baseUrl(appleAuthUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        ResponseEntity<String> responseEntity = webClient.post()
                .uri("/auth/token")
                .body(BodyInserters.fromFormData("client_id", requestBody.getClient_id())
                        .with("client_secret", requestBody.getClient_secret())
                        .with("code", requestBody.getCode())
                        .with("grant_type", requestBody.getGrant_type())
                        .with("redirect_uri", requestBody.getRedirect_uri()))
                .retrieve()
                .toEntity(String.class)
                .block();

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            JsonNode responseBody = objectMapper.readTree(responseEntity.getBody());
            return new AppleTokenResponse(HttpStatus.OK,
                    responseBody.path("access_token").asText(),
                    responseBody.path("expires_in").asLong(),
                    responseBody.path("id_token").asText(),
                    responseBody.path("refresh_token").asText(),
                    responseBody.path("token_type").asText());
        } else {
            throw new CustomException(CustomErrorCode.APPLE_TOKEN_RETRIEVE_FAILED);
        }

    }
}