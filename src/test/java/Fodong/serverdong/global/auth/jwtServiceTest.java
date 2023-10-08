package Fodong.serverdong.global.auth;

import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.memberToken.MemberToken;
import Fodong.serverdong.domain.memberToken.dto.response.ResponseTokenDto;
import Fodong.serverdong.domain.memberToken.repository.MemberTokenRepository;
import Fodong.serverdong.global.auth.enums.TokenStatus;
import Fodong.serverdong.global.auth.service.JwtService;
import Fodong.serverdong.global.exception.CustomErrorCode;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class jwtServiceTest {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberTokenRepository memberTokenRepository;

    private String testEmail = "test@example.com";
    private MemberToken newTestMemberToken;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;


    private static final String ACCESS_TOKEN = "AccessToken";
    private static final String REFRESH_TOKEN = "RefreshToken";
    private static final String USERID_CLAIM = "account_email";
    private static final String BEARER = "Bearer ";

    private Key secretKeySpec;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        Member newTestMember = Member.builder()
                .email(testEmail)
                .nickname("Test Nickname")
                .build();
        memberRepository.save(newTestMember);

        ResponseTokenDto accessTokenDto = jwtService.createAccessToken(newTestMember.getEmail());
        ResponseTokenDto refreshTokenDto = jwtService.createRefreshToken();

        newTestMemberToken = MemberToken.builder()
                .member(newTestMember)
                .accessToken(accessTokenDto.getToken())
                .accessExpiration(accessTokenDto.getExpiryDate())
                .refreshToken(refreshTokenDto.getToken())
                .refreshExpiration(refreshTokenDto.getExpiryDate())
                .build();
        memberTokenRepository.save(newTestMemberToken);
    }

    @Test
    @DisplayName("토큰으로 인증 정보 얻기")
    void testGetAuthentication() {

        String accessToken = newTestMemberToken.getAccessToken();

        Authentication authentication = jwtService.getAuthentication(accessToken);

        assertNotNull(authentication);
        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
        Object principal = authentication.getPrincipal();
        assertTrue(principal instanceof UserDetails);
        UserDetails userDetails = (UserDetails) principal;
        assertEquals(testEmail, userDetails.getUsername());

    }

    @Test
    @DisplayName("유효한 토큰 확인")
    void testIsTokenValid() {
        assertEquals(TokenStatus.VALID, jwtService.isAccessTokenValid(newTestMemberToken.getAccessToken()));
        assertEquals(TokenStatus.VALID, jwtService.isRefreshTokenValid(newTestMemberToken.getRefreshToken()));
    }


    @Test
    @DisplayName("유효하지 않은 토큰 확인")
    void testIsTokenInvalid() {
        assertThrows(JWTDecodeException.class, () -> {
            jwtService.isAccessTokenValid("invalidToken");
        });
        assertThrows(JWTDecodeException.class, () -> {
            jwtService.isRefreshTokenValid("invalidToken");
        });

    }

    @Test
    @DisplayName("유효한 access 토큰으로 요청")
    void testRequestWithValidToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/member/test")
                        .header("Authorization", "Bearer " + newTestMemberToken.getAccessToken()))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().string("User details received " + testEmail));

    }


    @Test
    @DisplayName("유효하지 않은 access 토큰으로 요청")
    void testRequestWithInvalidToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/member/test")
                        .header("Authorization", "Invalid Token"))
                .andExpect(jsonPath("$.code").value(CustomErrorCode.INVALID_TOKEN.toString()));
    }


    @Test
    @DisplayName("토큰 없이 요청")
    void testRequestWithoutToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/member/test"))
                .andExpect(status().isUnauthorized()) // 403 Forbidden
                .andExpect(jsonPath("$.code").value(CustomErrorCode.INVALID_TOKEN.toString()));
    }

    @Test
    @DisplayName("만료된 토큰으로 요청")
    void testRequestWithExpiredToken() throws Exception {

        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        secretKeySpec = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        LocalDateTime expiryDate = LocalDateTime.now().minusSeconds(10); // 10초 전에 만료
        Date expiryDateAsDate = java.sql.Timestamp.valueOf(expiryDate);

        String expiredAccessToken = JWT.create()
                .withSubject(ACCESS_TOKEN)
                .withExpiresAt(expiryDateAsDate)
                .withClaim(USERID_CLAIM, testEmail)
                .sign(Algorithm.HMAC512(secretKeySpec.getEncoded()));

        String expiredRefreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN)
                .withExpiresAt(expiryDateAsDate)
                .sign(Algorithm.HMAC512(secretKeySpec.getEncoded()));


        mockMvc.perform(get("/api/member/test")
                        .header("Authorization", "Bearer " + expiredAccessToken)
                        .header("Refresh", "Bearer " + expiredRefreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()) // 401
                .andExpect(jsonPath("$.code").value(CustomErrorCode.TOKEN_EXPIRED.toString()));

    }

    @Test
    @DisplayName("잘못된 이메일 정보의 토큰으로 요청")
    void testRequestWithInvalidEmailToken() throws Exception {

        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        secretKeySpec = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(accessExpiration);
        Date expiryDateAsDate = java.sql.Timestamp.valueOf(expiryDate);

        String wrongToken = JWT.create()
                .withSubject(ACCESS_TOKEN)
                .withExpiresAt(expiryDateAsDate)
                .withClaim(USERID_CLAIM, "invalidTest@example.com") // 저장되어 있지 않은 이메일
                .sign(Algorithm.HMAC512(secretKeySpec.getEncoded()));

        LocalDateTime refreshExpiryDate = LocalDateTime.now().plusSeconds(refreshExpiration);
        Date refreshExpiryDateAsDate = java.sql.Timestamp.valueOf(refreshExpiryDate);
        String validRefreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN)
                .withExpiresAt(refreshExpiryDateAsDate)
                .sign(Algorithm.HMAC512(secretKeySpec.getEncoded()));

        mockMvc.perform(get("/api/member/test")
                        .header("Authorization", "Bearer " + wrongToken)
                        .header("Refresh", "Bearer " + validRefreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(CustomErrorCode.MEMBER_NOT_FOUND.toString()));

    }


    @Test
    @DisplayName("만료된 엑세스 토큰, 유효한 리프레시 토큰으로 요청")
    void testRequestWithExpiredAccessToken() throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        secretKeySpec = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        // 만료된 Access Token 생성
        LocalDateTime accessExpiryDate = LocalDateTime.now().minusSeconds(10); // 10초 전에 만료
        Date accessExpiryDateAsDate = java.sql.Timestamp.valueOf(accessExpiryDate);
        String expiredAccessToken = JWT.create()
                .withSubject(ACCESS_TOKEN)
                .withExpiresAt(accessExpiryDateAsDate)
                .withClaim(USERID_CLAIM, testEmail)
                .sign(Algorithm.HMAC512(secretKeySpec.getEncoded()));

        // 유효한 Refresh Token 생성
        LocalDateTime refreshExpiryDate = LocalDateTime.now().plusSeconds(refreshExpiration);
        Date refreshExpiryDateAsDate = java.sql.Timestamp.valueOf(refreshExpiryDate);
        String validRefreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN)
                .withExpiresAt(refreshExpiryDateAsDate)
                .sign(Algorithm.HMAC512(secretKeySpec.getEncoded()));

        mockMvc.perform(get("/api/member/test")
                        .header("Authorization", "Bearer " + expiredAccessToken)
                        .header("Refresh", "Bearer " + validRefreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

//    @Test
//    @DisplayName("만료된 엑세스 토큰, 유효한 리프레시 토큰으로 요청, DB에 refresh token 정보 없을 때")
//    void testRequestWithExpiredAccessTokenError() throws Exception {
//        // Setup 로직 주석 후 진행
//
//        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
//        secretKeySpec = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
//
//        // 만료된 Access Token 생성
//        LocalDateTime accessExpiryDate = LocalDateTime.now().minusSeconds(10); // 10초 전에 만료
//        Date accessExpiryDateAsDate = java.sql.Timestamp.valueOf(accessExpiryDate);
//        String expiredAccessToken = JWT.create()
//                .withSubject(ACCESS_TOKEN)
//                .withExpiresAt(accessExpiryDateAsDate)
//                .withClaim(USERID_CLAIM, testEmail)
//                .sign(Algorithm.HMAC512(secretKeySpec.getEncoded()));
//
//        // 유효한 Refresh Token 생성
//        LocalDateTime refreshExpiryDate = LocalDateTime.now().plusSeconds(refreshExpiration);
//        Date refreshExpiryDateAsDate = java.sql.Timestamp.valueOf(refreshExpiryDate);
//        String validRefreshToken = JWT.create()
//                .withSubject(REFRESH_TOKEN)
//                .withExpiresAt(refreshExpiryDateAsDate)
//                .sign(Algorithm.HMAC512(secretKeySpec.getEncoded()));
//
//        mockMvc.perform(get("/api/member/test")
//                        .header("Authorization", "Bearer " + expiredAccessToken)
//                        .header("Refresh", "Bearer " + validRefreshToken)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value(CustomErrorCode.MEMBER_NOT_FOUND.toString()));
//    }


}

