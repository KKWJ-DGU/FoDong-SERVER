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
    RESTAURANT_NOT_FOUND(BAD_REQUEST,"존재하지 않는 식당 정보 입니다."),
    CATEGORY_NOT_CONTAIN(BAD_REQUEST,"존재하지 않는 카테고리ID가 포함되어 있습니다."),
    CATEGORY_NOT_FOUND(BAD_REQUEST,"존재하지 않는 카테고리 입니다."),
    MEMBER_NOT_FOUND(BAD_REQUEST, "해당 회원이 존재하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    KAKAO_LOGIN_FAILURE(BAD_REQUEST,"카카오 로그인에 실패하였습니다."),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");

    private final HttpStatus httpStatus;
    private final String message;


}
