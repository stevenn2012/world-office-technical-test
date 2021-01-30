package co.com.worldoffice.shoppingcart.service.imp;

import co.com.worldoffice.shoppingcart.domain.model.Product;
import co.com.worldoffice.shoppingcart.domain.model.User;
import co.com.worldoffice.shoppingcart.domain.repository.IProductRepository;
import co.com.worldoffice.shoppingcart.domain.repository.IUserRepository;
import co.com.worldoffice.shoppingcart.service.ICartService;
import co.com.worldoffice.shoppingcart.service.exception.ComponentException;
import co.com.worldoffice.shoppingcart.utils.interfaces.CorrelationIdAllocator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CartService implements ICartService, CorrelationIdAllocator {

    private final IProductRepository productRepository;
    private final IUserRepository userRepository;

    private static final String PRODUCT_NOT_HAS_STOCK = "Product not has stock";
    private static final String PRODUCT_NOT_FOUND = "Product not found";

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<Product> addProductToCart(String ip, int productId) throws ComponentException {
        assignCorrelative();
        log.info("Adding product with id [{}] to user [{}]", productId, ip);
        User user = findUser(ip);
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isEmpty()) {
            log.warn("Product with id [{}] not found", productId);
            throw new ComponentException(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        Product product = optionalProduct.get();
        if (product.notHasStock()) {
            log.warn("Product with id [{}] not has stock", productId);
            throw new ComponentException(PRODUCT_NOT_HAS_STOCK, HttpStatus.NO_CONTENT);
        }

        user.getProducts().add(optionalProduct.get());
        log.info("Added product to cart");
        return user.getProducts();
    }

    @Override
    public List<Product> findCartProducts(String ip) {
        assignCorrelative();
        log.info("Find cart products of user [{}]", ip);
        User user = findUser(ip);
        log.info("Find products is completed");
        return user.getProducts();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<Product> cleanCart(String ip) {
        assignCorrelative();
        log.info("Cleaning cart of user [{}]", ip);
        User user = findUser(ip);
        user.setProducts(new ArrayList<>());
        log.info("Cleaning completed");
        return user.getProducts();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<Product> buyProducts(String ip) throws ComponentException {
        assignCorrelative();
        log.info("Buy products of user [{}] cart", ip);
        User user = findUser(ip);
        for (Product product : user.getProducts()) {
            if (product.notHasStock()) {
                log.warn("Product with id [{}] not has stock now", product.getId());
                throw new ComponentException(PRODUCT_NOT_HAS_STOCK, HttpStatus.NO_CONTENT);
            }
            product.removeItemOfStock();
        }
        List<Product> products = user.getProducts();
        user.setProducts(new ArrayList<>());
        log.info("Buy of products end successfully");
        return products;
    }

    private synchronized User findUser(String ip) {
        return userRepository.findFirstByIp(ip).orElseGet(() -> createUser(ip));
    }

    private User createUser(String ip) {
        return userRepository.save(User.builder().ip(ip).products(new ArrayList<>()).build());
    }
}
