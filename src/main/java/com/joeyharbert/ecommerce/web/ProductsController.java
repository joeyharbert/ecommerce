package com.joeyharbert.ecommerce.web;

import com.joeyharbert.ecommerce.business.ProductsService;
import com.joeyharbert.ecommerce.data.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductsController {
    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping(path = "/products")
    public @ResponseBody List<Product> getProducts() { return this.productsService.getAllProducts(); }

    @GetMapping(path = "/products/{id}")
    public @ResponseBody Product getProductById(@PathVariable(value="id") Long id) { return this.productsService.getProductById(id); }

    @PostMapping(path = "/products")
    @ResponseStatus(HttpStatus.CREATED)
    public  @ResponseBody Product addProduct(@RequestBody Product product) { return this.productsService.addProduct(product); }
}
