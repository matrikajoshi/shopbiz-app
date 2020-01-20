package self.edu.shopbiz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by mpjoshi on 10/10/19.
 */

@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    private Integer orderedQuantities;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    private BigDecimal extPrice;


}
