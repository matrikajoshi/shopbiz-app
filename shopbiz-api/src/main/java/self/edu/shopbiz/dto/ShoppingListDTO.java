package self.edu.shopbiz.dto;



import lombok.Data;
import lombok.NoArgsConstructor;
import self.edu.shopbiz.model.Product;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mpjoshi on 9/30/19.
 */

@Data
@NoArgsConstructor
public class ShoppingListDTO {

    private Integer id;

    private String customerUserName;

    private Set<Product> products = new HashSet<>();

}
