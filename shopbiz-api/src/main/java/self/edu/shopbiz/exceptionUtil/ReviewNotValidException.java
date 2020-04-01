package self.edu.shopbiz.exceptionUtil;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class ReviewNotValidException extends RuntimeException {

    public ReviewNotValidException(String errorMessage) {
        super(errorMessage);
    }

}
