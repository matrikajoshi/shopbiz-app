package self.edu.shopbiz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by mpjoshi on 10/9/19.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product name is required.")
    @Basic(optional = false)
    @Size(min=2, message="Name should have at least 2 characters")
    private String name;

    @NotEmpty
    @Pattern(regexp="^[a-zA-Z0-9_]*$")
    @Column(name = "SKU")
    private String sku;

    @ManyToOne
    private Category category;

    private Boolean featured;

    @Positive
    private BigDecimal price;

    private String description;

    @Column(name="image_path")
    private String imageUrl;

    @Column(name="is_active")
    private Boolean active;

    private Integer availableQuantities;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public Product(Long id, @NotNull(message = "Product name is required.")
                            @Size(min = 2, message = "Name should have at least 2 characters") String name,
                   String sku,
                   @Positive BigDecimal price, String description, Integer availableQuantities) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.description = description;
        this.availableQuantities = availableQuantities;
    }

}
