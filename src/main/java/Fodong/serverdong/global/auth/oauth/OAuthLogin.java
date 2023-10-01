package Fodong.serverdong.global.auth.oauth;

import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.global.auth.dto.response.ResponseMemberTokenDto;

public interface OAuthLogin {

    ResponseMemberTokenDto getUserInfo(String code);
}
