package self.edu.shopbiz.model;

import javax.persistence.*;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by mpjoshi on 10/10/19.
 */

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long orderId;

    @OneToOne //eager by default
    private CartItem cartItem;

    private BigDecimal extPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }

    public BigDecimal getExtPrice() {
        return extPrice;
    }

    public void setExtPrice(BigDecimal extPrice) {
        this.extPrice = extPrice;
    }
}
