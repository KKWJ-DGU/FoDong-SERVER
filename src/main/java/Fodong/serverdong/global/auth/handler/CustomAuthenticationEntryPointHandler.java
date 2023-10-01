package Fodong.serverdong.global.auth.handler;

import Fodong.serverdong.global.auth.filter.JwtAuthenticationFilter;
import Fodong.serverdong.global.exception.CustomErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 토큰 없음, 시그니처 불일치
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("토큰이 존재하지 않거나 Bearer로 시작하지 않는 경우");
            CustomErrorCode errorCode = CustomErrorCode.INVALID_TOKEN;
            JwtAuthenticationFilter.setErrorResponse(response, errorCode);
        } else if (authorization.equals(CustomErrorCode.TOKEN_EXPIRED)) {
            log.error("토큰이 만료된 경우");

            // 토큰 만료
            CustomErrorCode errorCode = CustomErrorCode.TOKEN_EXPIRED;
            JwtAuthenticationFilter.setErrorResponse(response,errorCode);
        }
    }
}
