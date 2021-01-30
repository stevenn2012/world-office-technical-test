package co.com.worldoffice.shoppingcart.service;

import co.com.worldoffice.shoppingcart.domain.model.Product;
import co.com.worldoffice.shoppingcart.service.exception.ComponentException;

import java.util.List;

public interface ICartService {

    List<Product> addProductToCart(String ip, int productId) throws ComponentException;

    List<Product> findCartProducts(String ip) throws ComponentException;

    List<Product> cleanCart(String ip) throws ComponentException;

    List<Product> buyProducts(String ip) throws ComponentException;
}
