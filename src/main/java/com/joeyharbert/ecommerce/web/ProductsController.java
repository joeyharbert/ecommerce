package com.joeyharbert.ecommerce.web;

import com.joeyharbert.ecommerce.business.ProductsService;
import com.joeyharbert.ecommerce.data.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductsController {
    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping(path = "/products")
    public List<Product> getProducts() { return this.productsService.getAllProducts(); }
}
