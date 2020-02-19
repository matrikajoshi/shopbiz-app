package self.edu.shopbiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by mpjoshi on 12/27/19.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    private String id;

    private String sku;

    private BigDecimal discount;

    private LocalDate expDate;

}
