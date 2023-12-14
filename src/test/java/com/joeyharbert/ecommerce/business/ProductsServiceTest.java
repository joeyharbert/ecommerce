package com.joeyharbert.ecommerce.business;

import com.joeyharbert.ecommerce.data.Product;
import com.joeyharbert.ecommerce.data.ProductRepository;
import com.joeyharbert.ecommerce.data.Supplier;
import com.joeyharbert.ecommerce.data.SupplierRepository;
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
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductsServiceTest {
    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductsService productsService;

    Long id;
    String name;
    String description;

    int quantity;
    double price;
    Product testProduct;
    Supplier testSupplier;

    Map<String, Object> testMap;
    long supplierId;

    String missingProductErrorMessage;
    String missingSupplierErrorMessage;

    @BeforeAll
    public void setup() {
        id = 1L;
        name = "test name";
        description = "test description";
        quantity = 1;
        price = 9.99;
        supplierId = 1L;
        testSupplier = new Supplier("test name", "test@test.com", "555-555-5555");
        testSupplier.setId(supplierId);
        testProduct = new Product(name, price, description, quantity, testSupplier);
        testProduct.setId(id);
        missingProductErrorMessage = "Product does not exist";
        missingSupplierErrorMessage = "Supplier must exist";
        testMap = Map.of(
                "name", name,
                "price", price,
                "description", description,
                "quantity", quantity,
                "supplier_id", supplierId
        );
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

        Exception exception = assertThrows(RuntimeException.class, () -> {productsService.getProductById(4L);});
        String actualMessage = exception.getMessage();

        assertThat(missingProductErrorMessage).isEqualTo(actualMessage);
    }

    @Test
    public void givenNewProduct_whenAddProduct_thenReturnThatProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(testSupplier));

        Product result = productsService.addProduct(testMap);

        assertThat(result.getName()).isEqualTo(testProduct.getName());
        assertThat(result.getPrice()).isEqualTo(testProduct.getPrice());
        assertThat(result.getDescription()).isEqualTo(testProduct.getDescription());
        assertThat(result.getQuantity()).isEqualTo(testProduct.getQuantity());
        assertThat(result.getSupplier()).isEqualTo(testProduct.getSupplier());
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    public void givenNewProductWithBadSupplierId_whenAddProduct_thenThrowError() {
        when(supplierRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {productsService.addProduct(testMap);});
        String actualMessage = exception.getMessage();

        assertThat(missingSupplierErrorMessage).isEqualTo(actualMessage);
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

        String actualMessage = exception.getMessage();

        assertThat(missingProductErrorMessage).isEqualTo(actualMessage);
    }

    @Test
    public void givenProductId_whenDestroyProduct_thenDeleteCalled() throws RuntimeException {
        //happy path
        when(productRepository.findById(id)).thenReturn(Optional.of(testProduct));

        productsService.destroyProduct(id);

        verify(productRepository, times(1)).delete(testProduct);

        //sad path
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {productsService.destroyProduct(2L);});

        String actualMessage = exception.getMessage();

        assertThat(missingProductErrorMessage).isEqualTo(actualMessage);
    }
}
