package Fodong.serverdong.global.exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.auth0.jwt.exceptions.TokenExpiredException;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<CustomErrorResponse> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getCustomErrorCode());
        return CustomErrorResponse.toResponseEntity(e.getCustomErrorCode());
    }
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<CustomErrorResponse> handleTokenExpiredException(TokenExpiredException e) {
        CustomErrorCode errorCode = CustomErrorCode.TOKEN_EXPIRED;
        return CustomErrorResponse.toResponseEntity(errorCode);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleException(Exception e) {
        log.error("Unexpected error occurred: ", e);
        CustomErrorCode errorCode = CustomErrorCode.UNEXPECTED_ERROR;
        return CustomErrorResponse.toResponseEntity(errorCode);
    }

}
