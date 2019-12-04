package self.edu.shopbiz.dto;

import java.util.Date;

/**
 * Created by mpjoshi on 11/23/19.
 */

public class ErrorResponse {

    private Date timestamp;
    private String message;
    private String details;

    public ErrorResponse(Date timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public ErrorResponse(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }


}
