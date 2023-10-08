package Fodong.serverdong.domain.memberToken.dto.response;

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
