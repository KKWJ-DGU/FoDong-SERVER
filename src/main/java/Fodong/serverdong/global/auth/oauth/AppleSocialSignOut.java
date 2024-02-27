package Fodong.serverdong.global.auth.oauth;

import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AppleSocialSignOut {

    @Value("${oauth.provider.apple.client-id}")
    private String clientId;

    private final AppleSocialLogin appleSocialLogin;

    public void unlinkAppleAccount(String authorizationCode) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        String uriStr = "https://appleid.apple.com/auth/revoke";

        Map<String, String> params = new HashMap<>();
        params.put("client_secret", appleSocialLogin.getClientSecret());
        params.put("token", appleSocialLogin.getAccessToken(authorizationCode));
        params.put("client_id", clientId);

        try {
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI(uriStr))
                    .POST(getParamsUrlEncoded(params))
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new CustomException(CustomErrorCode.APPLE_UNLINK_FAILURE);
        }

    }

    private HttpRequest.BodyPublisher getParamsUrlEncoded(Map<String, String> parameters) {
        String urlEncoded = parameters.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        return HttpRequest.BodyPublishers.ofString(urlEncoded);
    }
}
