package co.com.worldoffice.shoppingcart.service;

import co.com.worldoffice.shoppingcart.domain.model.Product;
import org.springframework.data.domain.Page;

public interface IFindProductsService {

    Page<Product> findProducts(Integer page, Integer size, String name, String brand, Double lowestPrice, Double highestPrice);
}
