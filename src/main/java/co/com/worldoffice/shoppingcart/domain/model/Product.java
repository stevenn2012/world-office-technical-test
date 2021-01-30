package co.com.worldoffice.shoppingcart.domain.model;

import co.com.worldoffice.shoppingcart.domain.model.enums.ProductState;
import co.com.worldoffice.shoppingcart.utils.UtilsHelper;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@Table
@Entity
@Getter
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false)
    private String brand;

    @NonNull
    @Column(nullable = false)
    private double price;

    @NonNull
    @Column(nullable = false)
    private long quantityOnStock;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductState state;

    @NonNull
    @Column(nullable = false)
    private double discountPercent;

    public double getFinalPrice() {
        return UtilsHelper.getValueWithDiscount(price, discountPercent);
    }

    public boolean notHasStock() {
        return quantityOnStock <= 0;
    }

    public void removeItemOfStock() {
        quantityOnStock--;
    }
}
