package co.com.worldoffice.shoppingcart.controller;

import co.com.worldoffice.shoppingcart.domain.model.Product;
import co.com.worldoffice.shoppingcart.service.IFindProductsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${spring.application.root}")
@AllArgsConstructor
public class ProductController {

    private final IFindProductsService findProductsService;

    @GetMapping(path = "${spring.application.services.findProductsPath}")
    public ResponseEntity<Page<Product>> findProducts(@RequestParam(required = false) Integer page,
                                                      @RequestParam(required = false) Integer size,
                                                      @RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String brand,
                                                      @RequestParam(required = false) Double lowestPrice,
                                                      @RequestParam(required = false) Double highestPrice) {
        return ResponseEntity.ok(findProductsService.findProducts(page, size, name, brand, lowestPrice, highestPrice));
    }
}
