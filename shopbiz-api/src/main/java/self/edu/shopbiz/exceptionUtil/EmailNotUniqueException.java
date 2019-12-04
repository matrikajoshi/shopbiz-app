package self.edu.shopbiz.exceptionUtil;

/**
 * Created by mpjoshi on 11/23/19.
 */

public class EmailNotUniqueException  extends RuntimeException {

    public EmailNotUniqueException() {
        super();
    }

    public EmailNotUniqueException(String message) {
        super(message);
    }

    public EmailNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotUniqueException(Throwable cause) {
        super(cause);
    }
}
