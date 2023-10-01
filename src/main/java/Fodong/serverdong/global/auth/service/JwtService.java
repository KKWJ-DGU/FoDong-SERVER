package Fodong.serverdong.global.auth.service;

import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.global.auth.dto.response.FilterProcessingTokenDto;
import Fodong.serverdong.global.auth.dto.response.ResponseTokenDto;
import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import static javax.servlet.http.HttpServletResponse.*;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessExpiration;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN = "AccessToken";
    private static final String REFRESH_TOKEN = "RefreshToken";
    private static final String USERID_CLAIM = "account_email";
    private static final String BEARER = "Bearer ";
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;
    private static final String AUTHORITIES_KEY = "auth";

    private Key secretKeySpec;

    @PostConstruct
    private void initKeys() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        secretKeySpec = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    /**
     * AccessToken 생성
     * @param email 이메일
     */
    public ResponseTokenDto createAccessToken(String email) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusSeconds(accessExpiration);

        Date expiryDateAsDate = java.sql.Timestamp.valueOf(expiryDate);

        String token = JWT.create()
                .withSubject(ACCESS_TOKEN)
                .withExpiresAt(expiryDateAsDate)
                .withClaim(USERID_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKeySpec.getEncoded()));

        return new ResponseTokenDto(token, expiryDate);
    }

    public ResponseTokenDto createRefreshToken() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusSeconds(refreshExpiration);

        Date expiryDateAsDate = java.sql.Timestamp.valueOf(expiryDate);

        String token = JWT.create()
                .withSubject(REFRESH_TOKEN)
                .withExpiresAt(expiryDateAsDate)
                .sign(Algorithm.HMAC512(secretKeySpec.getEncoded()));

        return new ResponseTokenDto(token, expiryDate);
    }



    /**
     * AccessToken , RefreshToken 보내기
     */
    public void sendAccessAndRefreshToken(Member member, HttpServletResponse response, String accessToken, String refreshToken) throws IOException {

        response.setStatus(SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        FilterProcessingTokenDto loginMemberDto = new FilterProcessingTokenDto(accessToken,refreshToken);
        String res = objectMapper.writeValueAsString(loginMemberDto);
        response.getWriter().write(res);
    }


    /**
     * 사용자의 Authentication 객체를 반환
     */
    public Authentication getAuthentication(String accessToken) {

        DecodedJWT decodedJWT = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities = Collections.emptyList();

        UserDetails principal = new User(decodedJWT.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }



    /**
     * 액세스 토큰에서 Claims를 파싱
     * @param accessToken
     * @return Claims
     */
    private DecodedJWT parseClaims(String accessToken) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secretKey)).build();
            return verifier.verify(accessToken);
        } catch (TokenExpiredException e) {
            log.error("Token expired: {}", e.getMessage(), e);
            throw new CustomException(CustomErrorCode.TOKEN_EXPIRED);
        } catch (JWTVerificationException e) {
            log.error("Error verifying token: {}", e.getMessage(), e);
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }
    }


    /**
     * 헤더에서 RefreshToken 추출
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {

        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));


    }

    /**
     * 헤더에서 AccessToken 추출
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {


        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));


    }

    /**
     * AccessToken에서 Email추출
     */
    public Optional<String> extractUserEmail(String accessToken) {
        return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken)
                .getClaim(USERID_CLAIM)
                .asString());
    }


    /**
     * 토큰 유효성 검사
     */
    @SneakyThrows
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (TokenExpiredException e) {
            log.error("Token expired: {}", e.getMessage(), e);
            throw new CustomException(CustomErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            log.error("Unexpected error occurred while validating token: {}", e.getMessage(), e);
            throw new CustomException(CustomErrorCode.UNEXPECTED_ERROR);
        }
    }


}
