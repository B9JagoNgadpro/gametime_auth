package jagongadpro.autentikasi.controller;

import jagongadpro.autentikasi.dto.WebResponse;
import jagongadpro.autentikasi.model.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> constraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder().errors(exception.getMessage()).build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<WebResponse<String>> badCredentialException(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(WebResponse.<String>builder().errors("Email atau password salah").build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<WebResponse<String>> usernameNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(WebResponse.<String>builder().errors(exception.getMessage()).build());
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> responseStatusException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(WebResponse.<String>builder().errors(exception.getReason()).build());
    }
}

