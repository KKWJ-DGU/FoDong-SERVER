package Fodong.serverdong.global.auth.oauth;

import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class KakaoSocialSignOut {

    private final WebClient.Builder webClientBuilder;

    public void unlinkKakaoAccount(String accessToken) {
        final String KAKAO_UNLINK_URI = "https://kapi.kakao.com/v1/user/unlink";

        try {
            WebClient webClient = webClientBuilder.baseUrl(KAKAO_UNLINK_URI).build();

            String response = webClient.post()
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (WebClientResponseException e) {
            throw new CustomException(CustomErrorCode.KAKAO_UNLINK_FAILURE);
        }
    }
}