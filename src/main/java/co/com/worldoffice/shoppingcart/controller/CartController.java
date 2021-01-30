package co.com.worldoffice.shoppingcart.controller;

import co.com.worldoffice.shoppingcart.domain.model.Product;
import co.com.worldoffice.shoppingcart.service.ICartService;
import co.com.worldoffice.shoppingcart.service.exception.ComponentException;
import co.com.worldoffice.shoppingcart.utils.UtilsHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${spring.application.root}")
@AllArgsConstructor
public class CartController {

    private final ICartService cartService;

    @PostMapping(path = "${spring.application.services.addProductToCartPath}")
    public ResponseEntity<List<Product>> addProductToCart(ServerHttpRequest request, @PathVariable int productId) {
        try {
            return ResponseEntity.ok(cartService.addProductToCart(UtilsHelper.getIp(request), productId));
        } catch (ComponentException e) {
            return new ResponseEntity<>(new ArrayList<>(), e.getStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "${spring.application.services.getCartProductsPath}")
    public ResponseEntity<List<Product>> getCartProducts(ServerHttpRequest request) {
        try {
            return ResponseEntity.ok(cartService.findCartProducts(UtilsHelper.getIp(request)));
        } catch (ComponentException e) {
            return new ResponseEntity<>(new ArrayList<>(), e.getStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "${spring.application.services.cleanCartPath}")
    public ResponseEntity<List<Product>> cleanCartProducts(ServerHttpRequest request) {
        try {
            return ResponseEntity.ok(cartService.cleanCart(UtilsHelper.getIp(request)));
        } catch (ComponentException e) {
            return new ResponseEntity<>(new ArrayList<>(), e.getStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(path = "${spring.application.services.buyCartProductsPath}")
    public ResponseEntity<List<Product>> buyCartProducts(ServerHttpRequest request) {
        try {
            return ResponseEntity.ok(cartService.buyProducts(UtilsHelper.getIp(request)));
        } catch (ComponentException e) {
            return new ResponseEntity<>(new ArrayList<>(), e.getStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
