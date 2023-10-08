package Fodong.serverdong.global.auth.filter;

import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

import static Fodong.serverdong.global.auth.filter.JwtAuthenticationFilter.setErrorResponse;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    /**
     * 토큰 관련 에러 핸들링
     * JwtAuthenticationFilter 에서 발생하는 에러를 핸들링
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        try {
            filterChain.doFilter(request, response);

        } catch (TokenExpiredException e) {
            log.error("토큰이 만료되었습니다.");
            setErrorResponse(response, CustomErrorCode.TOKEN_EXPIRED);

        } catch (JwtException | IllegalArgumentException | JWTDecodeException e) {
            log.error("유효하지 않은 토큰입니다.");
            setErrorResponse(response, CustomErrorCode.INVALID_TOKEN);

        } catch (NoSuchElementException e) {
            log.error("사용자를 찾을 수 없습니다.");
            setErrorResponse(response, CustomErrorCode.MEMBER_NOT_FOUND);

        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("토큰을 추출할 수 없습니다.");
            setErrorResponse(response, CustomErrorCode.INVALID_TOKEN);

        } catch (CustomException e) {
            log.error("사용자 정의 예외 발생: {}", e.getCustomErrorCode());
            setErrorResponse(response, e.getCustomErrorCode());

        } catch (Exception e) {
            log.error("예상치 못한 오류가 발생했습니다: {}", e.getMessage());
            filterChain.doFilter(request, response);
        }



}

}
