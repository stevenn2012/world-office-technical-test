package co.com.worldoffice.shoppingcart.batch.dto;

import co.com.worldoffice.shoppingcart.domain.model.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class BatchStepsDTO {

    String correlativeID;

    String line;
    Product product;
}
