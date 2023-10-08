package Fodong.serverdong.domain.member.service;

import Fodong.serverdong.domain.member.enums.SocialType;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.memberToken.dto.response.ResponseMemberTokenDto;
import Fodong.serverdong.global.auth.oauth.KakaoSocialLogin;
import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomException;
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
     * @param accessToken 인가토큰
     */
    public ResponseMemberTokenDto socialUserInfo(String socialType, String accessToken) {
        SocialType type;
        try {
            type = SocialType.valueOf(socialType);
        } catch (IllegalArgumentException e) {
            throw new CustomException(CustomErrorCode.UNSUPPORTED_SOCIAL_TYPE);
        }

        switch (type) {
            case KAKAO:
                return kakaoSocialLogin.getUserInfo(accessToken);
//             case APPLE:
//                return appleSocialLogin.getUserInfo(code);
            default:
                throw new CustomException(CustomErrorCode.UNSUPPORTED_SOCIAL_TYPE);
        }
    }

}
