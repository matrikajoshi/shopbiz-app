package self.edu.shopbiz.dto;



import self.edu.shopbiz.model.Product;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mpjoshi on 9/30/19.
 */


public class ShoppingListDto {


    private Integer id;

    private String name;

    private String customerName;

    private Set<Product> products = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }


}
