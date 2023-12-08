package com.joeyharbert.ecommerce.web;

import com.joeyharbert.ecommerce.business.ProductsService;
import com.joeyharbert.ecommerce.data.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
public class ProductsController {
    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping(path = "/products")
    public @ResponseBody List<Product> getProducts() { return this.productsService.getAllProducts(); }

    @GetMapping(path = "/products/{id}")
    public @ResponseBody Product getProductById(@PathVariable(value="id") Long id) {
        try {
            return this.productsService.getProductById(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException((HttpStatus.NOT_FOUND), e.getMessage(), e);
        }
    }

    @PostMapping(path = "/products")
    @ResponseStatus(HttpStatus.CREATED)
    public  @ResponseBody Product addProduct(@RequestBody Product product) { return this.productsService.addProduct(product); }

    @PatchMapping(path = "/products/{id}")
    public @ResponseBody Product updateProduct(@RequestBody Map<String, Object> updates, @PathVariable(value="id") Long id) {
        try {
            return this.productsService.updateProduct(updates, id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping(path = "/products/{id}")
    public @ResponseBody String destroyProduct(@PathVariable(value="id") Long id) {
        try {
            this.productsService.destroyProduct(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

        return "Product destroyed.";
    }
}
