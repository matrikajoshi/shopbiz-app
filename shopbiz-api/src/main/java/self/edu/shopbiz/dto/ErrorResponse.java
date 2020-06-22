package self.edu.shopbiz.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by mpjoshi on 11/23/19.
 */

@Data
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

}
