package co.com.worldoffice.shoppingcart;

import co.com.worldoffice.shoppingcart.controller.CartController;
import co.com.worldoffice.shoppingcart.domain.model.Product;
import co.com.worldoffice.shoppingcart.domain.model.User;
import co.com.worldoffice.shoppingcart.domain.model.enums.ProductState;
import co.com.worldoffice.shoppingcart.domain.repository.IProductRepository;
import co.com.worldoffice.shoppingcart.domain.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@AutoConfigureDataJpa
@WebFluxTest(CartController.class)
@SpringJUnitWebConfig(Config.class)
class AddToCartTests {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private IProductRepository productRepository;

    @MockBean
    private IUserRepository userRepository;

    @BeforeEach
    public void generalMocks() {
        when(productRepository.findById(eq(1))).thenReturn(Optional.of(
                Product.builder().id(1).name("moto G8 plus 64gb").brand("motorola").price(649900).quantityOnStock(100)
                        .state(ProductState.USED).discountPercent(10).build()));

        when(productRepository.findById(eq(2))).thenReturn(Optional.of(
                Product.builder().id(2).name("moto G8 plus 64gb").brand("motorola").price(649900).quantityOnStock(0)
                        .state(ProductState.USED).discountPercent(10).build()));

        when(userRepository.findFirstByIp(any())).thenReturn(Optional.of(User.builder()
                .id(1)
                .ip("127.0.0.1")
                .products(new ArrayList<>())
                .build()));
    }

    @Test
    void notFoundProduct() {
        webClient.post()
                .uri("/world-office-api/shoppingCart/v1/cart/add/product/{productId}", 879)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void foundProductWithStock() {
        webClient.post()
                .uri("/world-office-api/shoppingCart/v1/cart/add/product/{productId}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    List<LinkedHashMap<String, Object>> productsOnCart = response.getResponseBody();
                    assertNotNull(productsOnCart);
                    assertEquals(1, productsOnCart.size());
                    assertEquals("moto G8 plus 64gb", productsOnCart.get(0).get("name"));
                    assertEquals("motorola", productsOnCart.get(0).get("brand"));
                    assertEquals(100, productsOnCart.get(0).get("quantityOnStock"));
                });
    }

    @Test
    void foundProductWithoutStock() {
        webClient.post()
                .uri("/world-office-api/shoppingCart/v1/cart/add/product/{productId}", 2)
                .exchange()
                .expectStatus().isNoContent();
    }
}
