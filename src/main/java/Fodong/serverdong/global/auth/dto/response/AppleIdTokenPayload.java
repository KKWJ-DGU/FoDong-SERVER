package Fodong.serverdong.global.auth.dto.response;

import lombok.Getter;

@Getter
public class AppleIdTokenPayload {
    private String sub;
    private String email;
}