package Fodong.serverdong.domain.member.service;

import Fodong.serverdong.domain.member.enums.SocialType;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.global.auth.dto.response.ResponseMemberTokenDto;
import Fodong.serverdong.global.auth.oauth.KakaoSocialLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final KakaoSocialLogin kakaoSocialLogin;

    /**
     * 소셜 로그인
     * @param socialType (KAKAO, APPLE) 소셜 로그인 타입
     * @param code 인가코드
     */
    public ResponseMemberTokenDto socialUserInfo(String socialType, String code) {
        SocialType type;
        try {
            type = SocialType.valueOf(socialType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported social type: " + socialType);
        }

        switch (type) {
            case KAKAO:
                return kakaoSocialLogin.getUserInfo(code);
            // case APPLE:
            //    return appleSocialLogin.getUserInfo(code);
            default:
                throw new IllegalArgumentException("Unsupported social type: " + socialType);
        }
    }


}
