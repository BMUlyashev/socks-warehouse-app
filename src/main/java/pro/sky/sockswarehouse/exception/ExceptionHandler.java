package pro.sky.sockswarehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(SocksException.class)
    public ResponseEntity<ApiException> handleSocksNotFoundException(SocksException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException exception = new ApiException(
                e.getMessage(),
                badRequest,
                LocalDateTime.now()
        );
        return ResponseEntity.status(badRequest).body(exception);
    }
}
