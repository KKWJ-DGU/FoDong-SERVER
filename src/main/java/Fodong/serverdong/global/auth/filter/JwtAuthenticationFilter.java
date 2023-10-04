package Fodong.serverdong.global.auth.filter;

import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.memberToken.MemberToken;
import Fodong.serverdong.domain.memberToken.repository.MemberTokenRepository;
import Fodong.serverdong.domain.memberToken.dto.response.ResponseTokenDto;
import Fodong.serverdong.global.auth.service.JwtService;
import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomErrorResponse;
import Fodong.serverdong.global.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String MAIN_URL = "/api/main";

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().contains(MAIN_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<String> refreshTokenOpt = jwtService.extractRefreshToken(request);
        if (refreshTokenOpt.isPresent()) {
            String refreshToken = refreshTokenOpt.get();
            if (jwtService.isTokenValid(refreshToken)) {
                checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
                filterChain.doFilter(request, response);
                return;
            } else {
                throw new CustomException(CustomErrorCode.TOKEN_EXPIRED);
            }
        }

        Optional<String> accessTokenOpt = jwtService.extractAccessToken(request);
        if (accessTokenOpt.isPresent()) {
            String accessToken = accessTokenOpt.get();
            if (jwtService.isTokenValid(accessToken)) {
                jwtService.extractUserEmail(accessToken).ifPresentOrElse(
                        email -> memberRepository.findByEmail(email).ifPresent(member -> saveAuthentication(accessToken)),
                        () -> {
                            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
                        }
                );
                filterChain.doFilter(request, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }


    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) throws IOException {
        MemberToken memberToken = memberTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(CustomErrorCode.MEMBER_NOT_FOUND));

        ResponseTokenDto reIssueRefreshToken = jwtService.createRefreshToken();
        ResponseTokenDto reIssueAccessToken = jwtService.createAccessToken(memberToken.getMember().getEmail());

        memberToken.updateTokens(reIssueAccessToken.getToken(), reIssueAccessToken.getExpiryDate(),
                reIssueRefreshToken.getToken(), reIssueRefreshToken.getExpiryDate());
        memberTokenRepository.saveAndFlush(memberToken);

        jwtService.sendAccessAndRefreshToken(response, reIssueAccessToken.getToken(), reIssueRefreshToken.getToken());
    }



    private void saveAuthentication(String accessToken) {
        Authentication authentication = jwtService.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Security Chain 에서 발생하는 에러 응답 구성
     */
    public static void setErrorResponse(HttpServletResponse response, CustomErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());

        ObjectMapper objectMapper = new ObjectMapper();
        CustomErrorResponse errorResponse = CustomErrorResponse.toResponseEntity(errorCode).getBody();

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
    }
}

