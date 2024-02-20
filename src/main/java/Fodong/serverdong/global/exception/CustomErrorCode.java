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
    KAKAO_RESPONSE_BODY_NULL(HttpStatus.BAD_REQUEST, "카카오 응답이 비어있습니다."),
    KAKAO_ACCOUNT_MISSING(HttpStatus.BAD_REQUEST, "kakao_account is missing in Kakao response"),
    UNSUPPORTED_SOCIAL_TYPE(BAD_REQUEST, "지원하지 않는 소셜 타입입니다."),
    MEMBER_TOKEN_NOT_FOUND(BAD_REQUEST,"해당 회원 토큰이 존재하지 않습니다."),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 않은 오류가 발생했습니다."),
    MEMBER_DUPLICATED_NICKNAME(BAD_REQUEST, "사용 중인 닉네임입니다."),
    REFRESH_TOKEN_MISSING(BAD_REQUEST, "리프레시 토큰이 없습니다."),
    ACCESS_TOKEN_MISSING(BAD_REQUEST, "엑세스 토큰이 없습니다."),
    KAKAO_UNLINK_FAILURE(BAD_REQUEST,"카카오 회원 탈퇴에 실패하였습니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 형식이 잘못되었습니다."),
    APPLE_TOKEN_RETRIEVE_FAILED(HttpStatus.BAD_REQUEST, "토큰 검색에 실패했습니다.");
    private final HttpStatus httpStatus;
    private final String message;


}


