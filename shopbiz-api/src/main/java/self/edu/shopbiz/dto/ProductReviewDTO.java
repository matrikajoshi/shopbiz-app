package self.edu.shopbiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewDTO implements Serializable
{
	private Double rating;

	private String headline;

	private String comment;

	private String userUserName;
}
