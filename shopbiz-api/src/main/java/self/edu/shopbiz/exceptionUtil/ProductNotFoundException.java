package self.edu.shopbiz.exceptionUtil;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException
{
	public ProductNotFoundException(final Long productId)
	{
		super("Product " + productId + " not found!");
	}
}
