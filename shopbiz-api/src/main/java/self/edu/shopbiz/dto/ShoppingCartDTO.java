package self.edu.shopbiz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class ShoppingCartDTO {

    private Long id;

    private Long userId;

    @JsonProperty("cartItems")
    private Set<CartItemDTO> cartItemsDTO = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
