package self.edu.couponService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by mpjoshi on 12/27/19.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "coupons")
public class Coupon {

    @Id
    private String id;

    @TextIndexed
    private String code;

    private BigDecimal discount;

    private LocalDate expDate;

}
