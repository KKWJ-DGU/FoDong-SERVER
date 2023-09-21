package Fodong.serverdong.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Builder
public class CustomErrorResponse {

    private final LocalDateTime localDateTime = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ResponseEntity<CustomErrorResponse> toResponseEntity(CustomErrorCode customErrorCode) {
        return ResponseEntity
                .status(customErrorCode.getHttpStatus())
                .body(CustomErrorResponse.builder()
                        .status(customErrorCode.getHttpStatus().value())
                        .error(customErrorCode.getHttpStatus().name())
                        .code(customErrorCode.name())
                        .message(customErrorCode.getMessage())
                        .build()
                );
    }
}
