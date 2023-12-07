package com.joeyharbert.ecommerce.business;

import com.joeyharbert.ecommerce.data.Product;
import com.joeyharbert.ecommerce.data.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductsServiceTest {
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductsService productsService;

    Long id;
    String name;
    String description;

    int quantity;
    double price;
    Product testProduct;

    @BeforeAll
    public void setup() {
        id = 1L;
        name = "test name";
        description = "test description";
        quantity = 1;
        price = 9.99;
        testProduct = new Product();
        testProduct.setId(id);
        testProduct.setName(name);
        testProduct.setDescription(description);
        testProduct.setQuantity(quantity);
        testProduct.setPrice(price);
    }

    @Test
    public void whenGetAllProducts_givenAllProducts() {
        List<Product> products = new ArrayList<>();
        products.add(testProduct);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productsService.getAllProducts();

        assertThat(result).isEqualTo(products);
    }

    @Test
    public void whenGetProductById_getProductWithThatId() {
        //Happy path
        when(productRepository.findById(id)).thenReturn(Optional.of(testProduct));

        Product result = productsService.getProductById(id);

        assertThat(result).isEqualTo(testProduct);

        //sad path
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        result = productsService.getProductById(4);

        assertThat(result.getId()).isEqualTo(0L);
    }

    @Test
    public void givenNewProduct_whenAddProduct_thenReturnThatProduct() {
        when(productRepository.save(testProduct)).thenReturn(testProduct);

        Product result = productsService.addProduct(testProduct);

        assertThat(result).isEqualTo(testProduct);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    public void givenProductUpdate_whenUpdateProduct_thenReturnUpdatedProduct() throws RuntimeException {
        //happy path
        when(productRepository.findById(id)).thenReturn(Optional.of(testProduct));
        Map<String, Object> updates = new HashMap<>();
        String updatedName = "updated name";
        updates.put("name", updatedName);
        Product result = productsService.updateProduct(updates, id);

        assertThat(result.getName()).isEqualTo(updatedName);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getUpdatedAt()).isNotNull();

        //sad path
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () ->  {productsService.updateProduct(updates, 2L);});

        String expectedMessage = "Product does not exist";
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
    }
}
