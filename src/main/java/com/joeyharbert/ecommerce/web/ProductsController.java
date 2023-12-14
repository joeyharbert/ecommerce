package com.joeyharbert.ecommerce.web;

import com.joeyharbert.ecommerce.business.ProductsService;
import com.joeyharbert.ecommerce.data.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "https://mini-capstone.peterxjang.com")
public class ProductsController {
    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping(path = "/products")
    public ResponseEntity<List<Product>> getProducts() { return new ResponseEntity<>(this.productsService.getAllProducts(), HttpStatus.OK); }

    @GetMapping(path = "/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(value="id") Long id) {
        try {
            Product product = this.productsService.getProductById(id);
            return new ResponseEntity<Product>(product, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new ResponseStatusException((HttpStatus.NOT_FOUND), e.getMessage(), e);
        }
    }

    @PostMapping(path = "/products")
    public  ResponseEntity<Product> addProduct(@RequestBody Map<String, Object> input) {
        try {
            Product finalProduct = this.productsService.addProduct(input);
            return new ResponseEntity<>(finalProduct, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PatchMapping(path = "/products/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Map<String, Object> updates, @PathVariable(value="id") Long id) {
        try {
            Product product = this.productsService.updateProduct(updates, id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping(path = "/products/{id}")
    public ResponseEntity<String> destroyProduct(@PathVariable(value="id") Long id) {
        try {
            this.productsService.destroyProduct(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

        return new ResponseEntity<>("Product destroyed.", HttpStatus.OK);
    }
}
