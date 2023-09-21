package Fodong.serverdong.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {

    SUCCESS(OK,"success"),
    RESTAURANT_NOT_FOUND(BAD_REQUEST,"존재하지 않는 식당 정보 입니다.");
    
    private final HttpStatus httpStatus;
    private final String message;

}
