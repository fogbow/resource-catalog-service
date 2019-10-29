package cloud.fogbow.rcs.api.http;

import cloud.fogbow.common.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class HttpExceptionToErrorConditionTranslator extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(UnauthorizedRequestException.class)
//    public final ResponseEntity<ExceptionResponse> handleAuthorizationException(Exception ex, WebRequest request) {
//
//        ExceptionResponse errorDetails = new ExceptionResponse(ex.getMessage(), request.getDescription(false));
//        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(UnauthenticatedUserException.class)
//    public final ResponseEntity<ExceptionResponse> handleAuthenticationException(Exception ex, WebRequest request) {
//
//        ExceptionResponse errorDetails = new ExceptionResponse(ex.getMessage(), request.getDescription(false));
//        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
//    }
}
