package co.com.worldoffice.shoppingcart.domain.repository;

import co.com.worldoffice.shoppingcart.domain.model.Product;
import co.com.worldoffice.shoppingcart.domain.repository.specification.FindProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProductRepository extends CrudRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    Optional<Product> findFirstByBrandAndName(String brand, String name);

    default Page<Product> findProducts(Integer page, Integer size, String name, String brand, Double lowestPrice, Double highestPrice) {
        return this.findAll(new FindProductSpecification(name, brand, lowestPrice, highestPrice), PageRequest.of(page, size));
    }
}
