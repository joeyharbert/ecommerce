package com.joeyharbert.ecommerce.business;

import com.joeyharbert.ecommerce.data.Product;
import com.joeyharbert.ecommerce.data.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductsService {
    private final ProductRepository productRepository;

    public ProductsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        Iterable<Product> products = productRepository.findAll();
        List<Product> productList = new ArrayList<>();
        products.forEach(productList::add);
        return productList;
    }
}
