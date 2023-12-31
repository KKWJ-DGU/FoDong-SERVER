package Fodong.serverdong.domain.member.service;

import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.memberToken.MemberToken;
import Fodong.serverdong.domain.memberToken.repository.MemberTokenRepository;
import Fodong.serverdong.domain.memberToken.dto.response.ResponseMemberTokenDto;
import Fodong.serverdong.global.auth.oauth.KakaoSocialLogin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.transaction.Transactional;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Transactional
class KakaoSocialLoginTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberTokenRepository memberTokenRepository;

    @Autowired
    private KakaoSocialLogin kakaoSocialLogin;

    private static final String kakaoAccessToken = "카카오 인가 토큰";

//    @Test
//    @DisplayName("카카오 소셜 로그인")
//    public void getUser() throws Exception {
//        // given
//        String email = "tlsdud8205@naver.com[KAKAO]";
//
//        ResponseMemberTokenDto result = kakaoSocialLogin.getUserInfo(kakaoAccessToken);
//
//        Optional<Member> foundMember = memberRepository.findByEmail(email);
//        assertTrue(foundMember.isPresent());
//        assertEquals(email, foundMember.get().getEmail());
//        Optional<MemberToken> foundToken = memberTokenRepository.findByMemberId(foundMember.get().getId());
//        assertTrue(foundToken.isPresent());
//
//        System.out.println(result.getAccessToken());
//        System.out.println(result.getRefreshToken());
//    }

}
