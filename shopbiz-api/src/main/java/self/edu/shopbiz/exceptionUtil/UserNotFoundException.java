package self.edu.shopbiz.exceptionUtil;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException
{
	public UserNotFoundException(Long userId)
	{
		super("User " + userId + " not found!");
	}
}
