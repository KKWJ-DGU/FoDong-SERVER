package Fodong.serverdong.global.auth.oauth;

import Fodong.serverdong.domain.member.*;
import Fodong.serverdong.domain.member.enums.SocialType;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.memberToken.MemberToken;
import Fodong.serverdong.domain.memberToken.repository.MemberTokenRepository;
import Fodong.serverdong.global.auth.dto.response.ResponseMemberTokenDto;
import Fodong.serverdong.global.auth.dto.response.ResponseTokenDto;
import Fodong.serverdong.global.auth.service.JwtService;
import Fodong.serverdong.global.exception.CustomException;
import Fodong.serverdong.global.exception.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KakaoSocialLogin implements OAuthLogin {

    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;
    private final JwtService jwtService;
    private final SocialType socialType = SocialType.KAKAO;
    private final WebClient.Builder webClientBuilder;


    @Override
    public ResponseMemberTokenDto getUserInfo(String accessToken) {
        String email = retrieveEmailFromKakao(accessToken);

        Member member = handleMemberAndToken(email);
        boolean isRegistered = memberRepository.findByEmail(email).isPresent();

        return new ResponseMemberTokenDto(
                memberTokenRepository.findByMemberId(member.getId()).orElseThrow().getAccessToken(),
                memberTokenRepository.findByMemberId(member.getId()).orElseThrow().getRefreshToken(),
                isRegistered
        );
    }

    private String retrieveEmailFromKakao(String accessToken) {
        try {
            WebClient webClient = webClientBuilder.baseUrl("https://kapi.kakao.com/v2/user/me").build();

            String responseBody = webClient.post()
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (responseBody == null) throw new JSONException("Response body is null");

            JSONObject jsonObj = new JSONObject(responseBody);
            JSONObject account = jsonObj.optJSONObject("kakao_account");

            if (account == null) throw new JSONException("kakao_account is missing in the response");

            return String.valueOf(account.get("email")) + "[" + socialType + "]";
        } catch (JSONException | WebClientResponseException e) {
            throw new CustomException(CustomErrorCode.KAKAO_LOGIN_FAILURE);
        }
    }



    private Member handleMemberAndToken(String email) {
        ResponseTokenDto accessResponseTokenDto = jwtService.createAccessToken(email);
        ResponseTokenDto refreshResponseTokenDto = jwtService.createRefreshToken();

        return memberRepository.findByEmail(email)
                .map(existingMember -> {
                    handleExistingUserToken(existingMember,
                            accessResponseTokenDto.getToken(),
                            refreshResponseTokenDto.getToken(),
                            accessResponseTokenDto.getExpiryDate(),
                            refreshResponseTokenDto.getExpiryDate());
                    return existingMember;
                })
                .orElseGet(() -> saveNewMember(
                        email,
                        accessResponseTokenDto.getToken(),
                        refreshResponseTokenDto.getToken(),
                        accessResponseTokenDto.getExpiryDate(),
                        refreshResponseTokenDto.getExpiryDate()));
    }

    private void handleExistingUserToken(Member existingMember, String accessToken, String refreshToken, LocalDateTime accessTokenExpiry, LocalDateTime refreshTokenExpiry) {
        Optional<MemberToken> optionalMemberToken = memberTokenRepository.findByMemberId(existingMember.getId());
        optionalMemberToken.ifPresent(existingMemberToken -> {
            existingMemberToken.updateTokens(accessToken, accessTokenExpiry, refreshToken, refreshTokenExpiry);
            memberTokenRepository.save(existingMemberToken);
        });
    }

    private Member saveNewMember(String email, String accessToken, String refreshToken, LocalDateTime accessTokenExpiry, LocalDateTime refreshTokenExpiry) {

        Member newKakaoMember = Member.builder()
                .email(email)
                .nickname("Temporary Nickname") // 임시 닉네임 할당
                .build();
        memberRepository.save(newKakaoMember);

        MemberToken newMemberToken = MemberToken.builder()
                .member(newKakaoMember)
                .accessToken(accessToken)
                .accessExpiration(accessTokenExpiry)
                .refreshToken(refreshToken)
                .refreshExpiration(refreshTokenExpiry)
                .build();
        memberTokenRepository.save(newMemberToken);

        return newKakaoMember;
    }
}
