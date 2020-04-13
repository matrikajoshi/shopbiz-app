package self.edu.shopbiz.exceptionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.access.AccessDeniedException;

import java.util.HashMap;

/**
 * Created by mpjoshi on 11/2/19.
 */


@ControllerAdvice
@Order(0)
public class SecurityExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public HashMap<String, String> handlePreauthorizeException(Exception ex) {
        logger.error(ex.getMessage(), ex);

        return new HashMap<String,String>(){{
            this.put("errorMessage", ex.getMessage());
        }};
    }


}
