package Fodong.serverdong.domain.memberToken.dto.response;

import lombok.Getter;


@Getter
public class ResponseMemberTokenDto {

    private String accessToken;
    private String refreshToken;
    private boolean isRegistered;

    public ResponseMemberTokenDto(String accessToken, String refreshToken, boolean isRegistered){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isRegistered = isRegistered;
    }
}
