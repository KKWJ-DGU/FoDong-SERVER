package Fodong.serverdong.global.auth.oauth;

import Fodong.serverdong.domain.member.*;
import Fodong.serverdong.domain.member.enums.SocialType;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.memberToken.MemberToken;
import Fodong.serverdong.domain.memberToken.dto.response.TokenInfoDto;
import Fodong.serverdong.domain.memberToken.repository.MemberTokenRepository;
import Fodong.serverdong.domain.memberToken.dto.response.ResponseMemberTokenDto;
import Fodong.serverdong.domain.memberToken.dto.response.ResponseTokenDto;
import Fodong.serverdong.global.auth.service.JwtService;
import Fodong.serverdong.global.exception.CustomException;
import Fodong.serverdong.global.exception.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class KakaoSocialLogin{

    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;
    private final JwtService jwtService;
    private final SocialType socialType = SocialType.KAKAO;
    private final WebClient.Builder webClientBuilder;


    /**
     * 사용자 정보 가져오기
     * @param accessToken 카카오 액세스 토큰
     * @return ResponseMemberTokenDto
     */
    public ResponseMemberTokenDto getUserInfo(String accessToken) {
        String email = retrieveEmailFromKakao(accessToken);
        return handleMemberAndToken(email);
    }

    /**
     * AccessToken으로 사용자 정보 얻기
     */
    private String retrieveEmailFromKakao(String accessToken) {
        try {
            WebClient webClient = webClientBuilder.baseUrl("https://kapi.kakao.com/v2/user/me").build();

            String responseBody = webClient.post()
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (responseBody == null) throw new CustomException(CustomErrorCode.KAKAO_RESPONSE_BODY_NULL);

            JSONObject jsonObj = new JSONObject(responseBody);
            JSONObject account = jsonObj.optJSONObject("kakao_account");

            if (account == null) throw new CustomException(CustomErrorCode.KAKAO_ACCOUNT_MISSING);

            return String.valueOf(account.get("email")) + "[" + socialType + "]";
        } catch (JSONException | WebClientResponseException e) {
            throw new CustomException(CustomErrorCode.KAKAO_LOGIN_FAILURE);
        }
    }

    /**
     * 회원 등록 처리
     */
    private ResponseMemberTokenDto handleMemberAndToken(String email) {
        ResponseTokenDto accessResponseTokenDto = jwtService.createAccessToken(email);
        ResponseTokenDto refreshResponseTokenDto = jwtService.createRefreshToken();

        TokenInfoDto tokenInfo = new TokenInfoDto(
                accessResponseTokenDto.getToken(),
                refreshResponseTokenDto.getToken(),
                accessResponseTokenDto.getExpiryDate(),
                refreshResponseTokenDto.getExpiryDate()
        );

        return memberRepository.findByEmail(email)
                .map(existingMember -> handleExistingUserToken(existingMember, tokenInfo))
                .orElseGet(() -> saveNewMember(email, tokenInfo));
    }


    /**
     * 기존 가입된 회원 처리
     */
    private ResponseMemberTokenDto handleExistingUserToken(Member existingMember, TokenInfoDto tokenInfo) {
        MemberToken existingMemberToken = memberTokenRepository.findByMemberId(existingMember.getId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.MEMBER_TOKEN_NOT_FOUND));

        existingMemberToken.updateTokens(tokenInfo.getAccessToken(), tokenInfo.getAccessTokenExpiry(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpiry());
        memberTokenRepository.save(existingMemberToken);

        return new ResponseMemberTokenDto(
                tokenInfo.getAccessToken(),
                tokenInfo.getRefreshToken(),
                true
        );
    }


    /**
     * 새로운 회원 처리
     */
    private ResponseMemberTokenDto saveNewMember(String email, TokenInfoDto tokenInfo) {
        Member newKakaoMember = Member.builder()
                .email(email)
                .nickname("Temporary Nickname")
                .build();
        memberRepository.save(newKakaoMember);

        MemberToken newMemberToken = MemberToken.builder()
                .member(newKakaoMember)
                .accessToken(tokenInfo.getAccessToken())
                .accessExpiration(tokenInfo.getAccessTokenExpiry())
                .refreshToken(tokenInfo.getRefreshToken())
                .refreshExpiration(tokenInfo.getRefreshTokenExpiry())
                .build();
        memberTokenRepository.save(newMemberToken);

        return new ResponseMemberTokenDto(
                tokenInfo.getAccessToken(),
                tokenInfo.getRefreshToken(),
                false
        );
    }

}
