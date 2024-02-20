package Fodong.serverdong.global.auth.dto.response;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class AppleTokenResponse {
    HttpStatus httpStatus;
    String accessToken;
    Long expiresIn;
    String idToken;
    String refreshToken;
    String tokenType;

    public AppleTokenResponse(HttpStatus httpStatus, String accessToken, Long expires_in, String idToken, String refreshToken, String tokenType){
        this.httpStatus = httpStatus;
        this.accessToken = accessToken;
        this.expiresIn = expires_in;
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
    }
}