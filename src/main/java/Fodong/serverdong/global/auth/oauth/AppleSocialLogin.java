package Fodong.serverdong.global.auth.oauth;

import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.member.enums.SocialType;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.memberToken.MemberToken;
import Fodong.serverdong.domain.memberToken.dto.response.ResponseMemberTokenDto;
import Fodong.serverdong.domain.memberToken.dto.response.ResponseTokenDto;
import Fodong.serverdong.domain.memberToken.dto.response.TokenInfoDto;
import Fodong.serverdong.domain.memberToken.repository.MemberTokenRepository;
import Fodong.serverdong.global.auth.dto.request.AppleTokenRequestBody;
import Fodong.serverdong.global.auth.dto.response.AppleIdTokenPayload;
import Fodong.serverdong.global.auth.dto.response.AppleTokenResponse;
import Fodong.serverdong.global.auth.service.JwtService;
import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AppleSocialLogin {

    @Value("${oauth.provider.apple.grant-type}")
    private String grantType;

    @Value("${oauth.provider.apple.client-id}")
    private String clientId;

    @Value("${oauth.provider.apple.key-id}")
    private String keyId;

    @Value("${oauth.provider.apple.team-id}")
    private String teamId;

    @Value("${oauth.provider.apple.auth-url}")
    private String audience;

    @Value("${oauth.provider.apple.private-key}")
    private String privateKey;

    @Value("${oauth.provider.apple.redirect-url}")
    private String redirectUrl;

    private final AppleClient appleClient;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;
    private final SocialType socialType = SocialType.APPLE;


    /**
     * Apple 로그인 후 사용자 정보 처리 및 토큰 발급
     */
    public ResponseMemberTokenDto handleAppleSocialLogin(String authorizationCode) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        AppleIdTokenPayload payload = getMemberInfo(authorizationCode);
        String email = String.valueOf(payload.getEmail()) + "[" + socialType + "]";

        // JWT 발급
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
     * 기존 회원 처리 및 토큰 갱신
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
     * 새로운 회원 등록 및 토큰 발급
     */
    private ResponseMemberTokenDto saveNewMember(String email, TokenInfoDto tokenInfo) {
        Member newKakaoMember = Member.builder()
                .email(email)
                .nickname(email)
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

    /**
     * Apple 인증 코드로 사용자 정보 조회
     */
    private AppleIdTokenPayload getMemberInfo(String authorizationCode) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (authorizationCode == null) {
            throw new CustomException(CustomErrorCode.APPLE_AUTHORIZATION_CODE_NULL);
        }

        String clientSecret = getClientSecret();
        AppleTokenRequestBody requestBody = new AppleTokenRequestBody(clientId, clientSecret, grantType, authorizationCode, redirectUrl);
        AppleTokenResponse tokenResponse = appleClient.appleTokenResponse(requestBody);

        return decodeAppleIdTokenPayload(tokenResponse.getIdToken(), AppleIdTokenPayload.class);

    }

    /**
     * Apple ID 토큰으로 사용자 정보 디코딩
     */
    private <T> T decodeAppleIdTokenPayload(String idToken, Class<T> targetClass) {
        String[] tokenParts = idToken.split("\\.");
        String payloadJWT = tokenParts[1];
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(payloadJWT));
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.readValue(payload, targetClass);
        } catch (Exception e) {
            throw new CustomException(CustomErrorCode.APPLE_TOKEN_PAYLOAD_DECODING_FAILED);
        }
    }

    /**
     * 클라이언트 시크릿 생성
     */
    private String getClientSecret() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Date expiration = Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setHeaderParam("alg", "ES256")
                .setHeaderParam("kid", keyId)
                .setIssuer(teamId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .setAudience(audience)
                .setSubject(clientId)
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    /**
     * PrivateKey 생성
     */
    private PrivateKey getPrivateKey() {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);

            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new RuntimeException("Error converting private key from String", e);
        }
    }

    /**
     * Apple 로그인 테스트 페이지 URL 생성
     */

    public String getAppleLogin() {
        return audience + "/auth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUrl
                + "&response_type=code%20id_token&scope=name%20email&response_mode=form_post";
    }
}