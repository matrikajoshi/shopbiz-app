package self.edu.shopbiz.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UserDTO {


    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 3, message = "Length must be more than 3")
    private String password;

    @Size(min=2, message="Name should have at least 2 characters")
    private String userName;
}
