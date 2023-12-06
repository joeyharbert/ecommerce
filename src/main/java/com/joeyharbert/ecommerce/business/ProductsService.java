package com.joeyharbert.ecommerce.business;

import com.joeyharbert.ecommerce.data.Product;
import com.joeyharbert.ecommerce.data.ProductRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Product getProductById(long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        return productOptional.orElseGet(Product::new);
    }

    public Product addProduct(Product inputProduct) {
        if (null == inputProduct) {
            throw new RuntimeException("Product cannot be null");
        }

        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        inputProduct.setCreatedAt(currentDate);
        inputProduct.setUpdatedAt(currentDate);

        this.productRepository.save(inputProduct);
        return inputProduct;
    }
}
