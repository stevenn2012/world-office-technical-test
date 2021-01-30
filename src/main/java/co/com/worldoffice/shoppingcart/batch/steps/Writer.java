package co.com.worldoffice.shoppingcart.batch.steps;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import co.com.worldoffice.shoppingcart.batch.dto.BatchStepsDTO;
import co.com.worldoffice.shoppingcart.domain.model.Product;
import co.com.worldoffice.shoppingcart.domain.repository.IProductRepository;
import co.com.worldoffice.shoppingcart.utils.interfaces.CorrelationIdAllocator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class Writer implements ItemWriter<Mono<BatchStepsDTO>>, CorrelationIdAllocator {

    private final IProductRepository productRepository;

    @Override
    public void write(List<? extends Mono<BatchStepsDTO>> batchStepsDTOList) {
        AtomicReference<String> correlationId = new AtomicReference<>();
        batchStepsDTOList.forEach(productFlux -> productFlux
                .map(productDTO -> {
                    correlationId.set(productDTO.getCorrelativeID());
                    return save(productDTO.getCorrelativeID(), productDTO.getProduct());
                })
                .doOnNext(product -> {
                    if (product.getId() != null) {
                        log.info("Product saved successfully");
                    }
                })
                .subscribe());
    }

    private synchronized Product save(String correlationId, Product product) {
        assignCorrelative(correlationId);

        log.debug("Saving product...");
        Optional<Product> optionalPersistedProduct = productRepository.findFirstByBrandAndName(product.getBrand(), product.getName());
        if (optionalPersistedProduct.isPresent()) {
            log.error("Product already exist");
            return product;
        }
        return productRepository.save(product);
    }
}