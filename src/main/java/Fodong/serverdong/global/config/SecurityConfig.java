package Fodong.serverdong.global.config;

import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.memberToken.repository.MemberTokenRepository;
import Fodong.serverdong.global.auth.filter.ExceptionHandlerFilter;
import Fodong.serverdong.global.auth.filter.JwtAuthenticationFilter;
import Fodong.serverdong.global.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().antMatchers(
                "/v3/api-docs",
                "/swagger-ui/**"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .cors()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/v3/**","/swagger-ui/**" , "/v3/api-docs/**", "/swagger-ui.html")
                .authenticated()

                // JWT 토큰 예외 처리
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, memberTokenRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class);

        return http.build();
    }
}
