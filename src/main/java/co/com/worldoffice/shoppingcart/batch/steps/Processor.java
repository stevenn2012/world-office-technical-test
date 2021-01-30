package co.com.worldoffice.shoppingcart.batch.steps;

import co.com.worldoffice.shoppingcart.batch.dto.BatchStepsDTO;
import co.com.worldoffice.shoppingcart.domain.model.Product;
import co.com.worldoffice.shoppingcart.domain.model.enums.ProductState;
import co.com.worldoffice.shoppingcart.utils.interfaces.CorrelationIdAllocator;
import co.com.worldoffice.shoppingcart.utils.UtilsHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class Processor implements ItemProcessor<BatchStepsDTO, Mono<BatchStepsDTO>>, CorrelationIdAllocator {

    private static final int CSV_COLUMNS_COUNT = 6;
    private static final List<Integer> FIELDS_TYPE_DOUBLE = List.of(2, 5);
    private static final List<Integer> FIELDS_TYPE_LONG = List.of(3);

    private static final int PRODUCT_NAME_POSITION = 0;
    private static final int PRODUCT_BRAND_POSITION = 1;
    private static final int PRODUCT_PRICE_POSITION = 2;
    private static final int PRODUCT_QUANTITY_ON_STOCK_POSITION = 3;
    private static final int PRODUCT_STATE_POSITION = 4;
    private static final int PRODUCT_DISCOUNT_PERCENT_POSITION = 5;

    @Override
    public Mono<BatchStepsDTO> process(BatchStepsDTO fileLinesDTO) {
        return Mono.just(fileLinesDTO)
                .flatMap(dto -> validateProduct(fileLinesDTO.getCorrelativeID(), fileLinesDTO.getLine()))
                .map(columnValues -> createProduct(fileLinesDTO.getCorrelativeID(), columnValues))
                .map(product -> fileLinesDTO.toBuilder().product(product).build());
    }

    private Mono<String[]> validateProduct(String correlationId, String line) {
        return Mono.just(correlationId).flatMap(correlative -> {
            assignCorrelative(correlationId);
            log.debug("Validating line...");

            String[] columnValues = line.split(",");

            if (columnValues.length != CSV_COLUMNS_COUNT) {
                log.error("Line not has the correct number of columns");
                return Mono.empty();
            }

            for (int i = 0; i < columnValues.length; i++) {
                if (columnValues[i] == null || columnValues[i].isBlank()) {
                    log.error("Line column [{}] is empty", i);
                    return Mono.empty();
                }

                if (FIELDS_TYPE_DOUBLE.contains(i) && UtilsHelper.isNotDouble(columnValues[i]) ||
                        FIELDS_TYPE_LONG.contains(i) && UtilsHelper.isNotNumber(columnValues[i])) {
                    log.error("Line column [{}] has invalid format", i);
                    return Mono.empty();
                }
            }

            return Mono.just(columnValues);
        });
    }

    private Product createProduct(String correlationId, String[] columnValues) {
        assignCorrelative(correlationId);
        log.debug("Creating product...");

        return Product.builder()
                .name(columnValues[PRODUCT_NAME_POSITION])
                .brand(columnValues[PRODUCT_BRAND_POSITION])
                .price(Double.parseDouble(columnValues[PRODUCT_PRICE_POSITION]))
                .quantityOnStock(Long.parseLong(columnValues[PRODUCT_QUANTITY_ON_STOCK_POSITION]))
                .state(ProductState.getByValue(columnValues[PRODUCT_STATE_POSITION]))
                .discountPercent(Double.parseDouble(columnValues[PRODUCT_DISCOUNT_PERCENT_POSITION]))
                .build();
    }
}