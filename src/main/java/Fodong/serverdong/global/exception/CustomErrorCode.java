package Fodong.serverdong.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.OK;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {

    SUCCESS(OK,"success");
    
    private final HttpStatus httpStatus;
    private final String message;

}
