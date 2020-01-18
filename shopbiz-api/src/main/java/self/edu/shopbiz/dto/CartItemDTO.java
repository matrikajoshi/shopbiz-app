package self.edu.shopbiz.dto;

import self.edu.shopbiz.model.Product;

import java.math.BigDecimal;

/**
 * Created by mpjoshi on 11/12/19.
 */

public class CartItemDTO {

    private Integer id;
    private Product product;
    private Integer quantity;
    private BigDecimal totalPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
