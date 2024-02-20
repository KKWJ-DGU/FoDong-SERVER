package Fodong.serverdong.global.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class AppleTokenRequestBody {
    String client_id;
    String client_secret;
    String grant_type;
    String code;
    String redirect_uri;

}