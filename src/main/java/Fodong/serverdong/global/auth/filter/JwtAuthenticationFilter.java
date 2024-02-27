package Fodong.serverdong.global.auth.filter;


import Fodong.serverdong.domain.memberToken.repository.MemberTokenRepository;
import Fodong.serverdong.global.auth.enums.TokenStatus;
import Fodong.serverdong.global.auth.service.JwtService;
import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomErrorResponse;
import Fodong.serverdong.global.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String LOGIN_URL = "/api/member/login/oauth";
    private static final String SWAGGER_UI_URL = "/fodong/swagger-ui";
    private static final String TOKEN_REISSUE_URL = "/api/membertoken/reissue";
    private static final String APPLE_TEST = "/api/member/apple/callback";
    private static final String APPLE_TEST2 = "/apple/home";
    private final JwtService jwtService;
    private final MemberTokenRepository memberTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if(requestURI.contains(LOGIN_URL) || requestURI.contains(SWAGGER_UI_URL) || requestURI.contains(APPLE_TEST) || requestURI.contains(APPLE_TEST2)){
//        if(requestURI.contains(LOGIN_URL) || requestURI.contains(SWAGGER_UI_URL)){
            filterChain.doFilter(request,response);
            return;
        }

        response.setCharacterEncoding("utf-8");

        String refreshToken = jwtService.extractRefreshToken(request);
        if (requestURI.contains(TOKEN_REISSUE_URL) && refreshToken == null) {
            throw new CustomException(CustomErrorCode.REFRESH_TOKEN_MISSING);
        }

        if (refreshToken != null && jwtService.isTokenValid(refreshToken) == TokenStatus.VALID) {
            jwtService.checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        String accessToken = jwtService.extractAccessToken(request);
        if (accessToken == null) {
            throw new CustomException(CustomErrorCode.ACCESS_TOKEN_MISSING);
        }

        TokenStatus accessTokenStatus = jwtService.isTokenValid(accessToken);
        if (accessTokenStatus == TokenStatus.VALID) {
            saveAuthentication(accessToken);
            filterChain.doFilter(request, response);
        }

    }

    private void saveAuthentication(String accessToken) {
        Authentication authentication = jwtService.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Security Chain에서 발생하는 에러 응답 구성
     */
    public static void setErrorResponse(HttpServletResponse response, CustomErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        CustomErrorResponse errorResponse = CustomErrorResponse.toResponseEntity(errorCode).getBody();

        if (errorResponse != null) {
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
        }
    }

}

