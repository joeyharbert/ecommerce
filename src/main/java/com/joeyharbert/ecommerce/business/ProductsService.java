package com.joeyharbert.ecommerce.business;

import com.joeyharbert.ecommerce.data.Product;
import com.joeyharbert.ecommerce.data.ProductRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        if (productOptional.isEmpty()) {
            throw new RuntimeException("Product does not exist");
        }

        return productOptional.get();
    }

    public Product addProduct(Product inputProduct) {
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        inputProduct.setCreatedAt(currentDate);
        inputProduct.setUpdatedAt(currentDate);

        this.productRepository.save(inputProduct);
        return inputProduct;
    }

    public Product updateProduct(Map<String, Object> updates, Long id) throws RuntimeException {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            throw new RuntimeException("Product does not exist");
        }
        Product product = productOptional.get();
        product.setName(null == updates.get("name") ? product.getName() : (String) updates.get("name"));
        product.setPrice(null == updates.get("price") ? product.getPrice() : (double) updates.get("price"));
        product.setDescription(null == updates.get("description") ? product.getDescription() : (String) updates.get("description"));
        product.setQuantity(null == updates.get("quantity") ? product.getQuantity() : (int) updates.get("quantity"));
        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        productRepository.save(product);
        return product;
    }

    public void destroyProduct(Long id) throws RuntimeException {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            productRepository.delete(product);
        } else {
            throw new RuntimeException("Product does not exist");
        }
    }
}
