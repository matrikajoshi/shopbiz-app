package self.edu.shopbiz.exceptionUtil;

import org.modelmapper.spi.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import self.edu.shopbiz.dto.ErrorResponse;

import java.util.Date;
import java.util.List;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public  ResponseEntity<Object> handlePreauthorizeException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage());
        return new ResponseEntity(errorResponse, HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), "Validation Failed",
                ex.getBindingResult().toString());
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(ResourceNotFoundException ex,
                                                WebRequest request) {
        ErrorResponse exceptionResponse = new ErrorResponse(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailNotUniqueException.class)
    public final ResponseEntity<Object> handleEmailNotUniqueException(EmailNotUniqueException ex,
                                                                    WebRequest request) {
        ErrorResponse exceptionResponse = new ErrorResponse(new Date(), ex.getMessage(),
        request.getDescription(true));
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleBaseException(DataIntegrityViolationException e){
        ErrorResponse errorResponse = new ErrorResponse(new Date(), e.getMessage());
        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }


}
