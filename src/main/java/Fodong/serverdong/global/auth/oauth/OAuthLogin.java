package Fodong.serverdong.global.auth.oauth;

import Fodong.serverdong.domain.memberToken.dto.response.ResponseMemberTokenDto;

public interface OAuthLogin {

    ResponseMemberTokenDto getUserInfo(String code);
}
