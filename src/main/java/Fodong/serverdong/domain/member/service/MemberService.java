package Fodong.serverdong.domain.member.service;

import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.member.dto.response.ResponseMemberInfoDto;
import Fodong.serverdong.domain.member.enums.SocialType;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.memberToken.dto.response.ResponseMemberTokenDto;
import Fodong.serverdong.domain.memberToken.repository.MemberTokenRepository;
import Fodong.serverdong.domain.restaurant.Restaurant;
import Fodong.serverdong.domain.restaurant.repository.RestaurantRepository;
import Fodong.serverdong.domain.restaurantCategory.RestaurantCategory;
import Fodong.serverdong.domain.wishlist.Wishlist;
import Fodong.serverdong.domain.wishlist.repository.WishlistRepository;
import Fodong.serverdong.global.auth.oauth.KakaoSocialLogin;
import Fodong.serverdong.global.auth.oauth.KakaoSocialSignOut;
import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;
    private final WishlistRepository wishlistRepository;
    private final RestaurantRepository restaurantRepository;
    private final KakaoSocialLogin kakaoSocialLogin;
    private final KakaoSocialSignOut kakaoSocialSignOut;

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

    /**
     * 닉네임 설정
     * @param userEmail 현재 유저의 아이디 또는 이름
     * @param nickname 설정하려는 닉네임
     */
    public void setNickname(String userEmail, String nickname) {
        if (isNicknameDuplicate(nickname)) {
            throw new CustomException(CustomErrorCode.MEMBER_DUPLICATED_NICKNAME);
        }

        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(CustomErrorCode.MEMBER_NOT_FOUND));

        member.updateNickname(nickname);
        memberRepository.save(member);
    }

    /**
     * 닉네임 중복 검사
     * @param nickname 검사하려는 닉네임
     * @return 중복 여부
     */
    private boolean isNicknameDuplicate(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }

    /**
     * 회원 정보 조회
     */
    @Transactional
    public ResponseMemberInfoDto getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.MEMBER_NOT_FOUND));
        return new ResponseMemberInfoDto(member.getNickname());
    }

    @Transactional
    public void deleteMember(Long memberId, String accessToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.MEMBER_NOT_FOUND));

        SocialType socialType = extractSocialTypeFromEmail(member.getEmail());
        unlinkSocialAccount(socialType, accessToken);

        // wishlist 삭제 및 wishCount 감소
        deleteWishlists(member);

        // MemberToken 삭제
        memberTokenRepository.findByMemberId(memberId)
                .ifPresent(memberTokenRepository::delete);

        // Member 삭제
        memberRepository.delete(member);
    }

    private SocialType extractSocialTypeFromEmail(String email) {
        int start = email.indexOf('[');
        int end = email.indexOf(']');

        if (start == -1 || end == -1 || start >= end) {
            throw new CustomException(CustomErrorCode.INVALID_EMAIL_FORMAT);
        }

        String socialTypeStr = email.substring(start + 1, end).toUpperCase();
        try {
            return SocialType.valueOf(socialTypeStr);
        } catch (IllegalArgumentException e) {
            throw new CustomException(CustomErrorCode.UNSUPPORTED_SOCIAL_TYPE);
        }
    }


    private void unlinkSocialAccount(SocialType socialType, String accessToken) {
        switch (socialType) {
            case KAKAO:
                kakaoSocialSignOut.unlinkKakaoAccount(accessToken);
                break;
            case APPLE:
                // appleSocialSignOut.unlinkAppleAccount(accessToken);
                break;
            default:
                throw new CustomException(CustomErrorCode.UNSUPPORTED_SOCIAL_TYPE);
        }
    }

    private void deleteWishlists(Member member) {
        List<Wishlist> wishlists = wishlistRepository.findByMember(member);
        for (Wishlist wishlist : wishlists) {
            RestaurantCategory restaurantCategory = wishlist.getRestaurantCategory();
            Restaurant restaurant = restaurantCategory.getRestaurant();

            // wishCount 감소
            restaurant.decreaseWishCount();
            restaurantRepository.save(restaurant);

            // Wishlist 삭제
            wishlistRepository.delete(wishlist);
        }
    }
}
