package co.com.worldoffice.shoppingcart.service.imp;

import co.com.worldoffice.shoppingcart.domain.model.Product;
import co.com.worldoffice.shoppingcart.domain.repository.IProductRepository;
import co.com.worldoffice.shoppingcart.service.IFindProductsService;
import co.com.worldoffice.shoppingcart.utils.interfaces.CorrelationIdAllocator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class FindProductsService implements IFindProductsService, CorrelationIdAllocator {

    private final IProductRepository productRepository;

    @Override
    public Page<Product> findProducts(Integer page, Integer size, String name, String brand, Double lowestPrice, Double highestPrice) {
        assignCorrelative();
        if (page == null) {
            page = 0;
        }

        if (size == null) {
            size = 10;
        }

        log.info("Finding product with params: [Page={}, Size={}, name={}, brand={}, lowestPrice={}, highestPrice={}]",
                page, size, name, brand, lowestPrice, highestPrice);

        return productRepository.findProducts(page, size, name, brand, lowestPrice, highestPrice);
    }
}
