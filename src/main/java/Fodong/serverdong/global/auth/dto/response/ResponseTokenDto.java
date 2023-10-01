package Fodong.serverdong.global.auth.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ResponseTokenDto {

    private String token;
    private LocalDateTime expiryDate;

    public ResponseTokenDto(String token, LocalDateTime expiryDate){
        this.token=token;
        this.expiryDate=expiryDate;
    }

}
