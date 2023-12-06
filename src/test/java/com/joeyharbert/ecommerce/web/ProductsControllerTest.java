package com.joeyharbert.ecommerce.web;

import com.joeyharbert.ecommerce.business.ProductsService;
import com.joeyharbert.ecommerce.data.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductsController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
    public void givenProducts_whenGetProducts_thenStatus200() throws Exception {
        ArrayList<Product> testproducts = new ArrayList<>();
        testproducts.add(testProduct);
        when(productsService.getAllProducts()).thenReturn(testproducts);

        MvcResult result = this.mockMvc.perform(get("/products")).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertThat(response).isEqualTo("[{\"id\":1,\"name\":\"test name\",\"price\":9.99,\"description\":\"test description\",\"quantity\":1,\"createdAt\":null,\"updatedAt\":null}]");
    }

    @Test
    public void givenProduct_whenGetProduct_thenStatus200() throws Exception {
        when(productsService.getProductById(anyLong())).thenReturn(testProduct);

        MvcResult result = this.mockMvc.perform(get("/products/{id}", 1)).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertThat(response).isEqualTo("{\"id\":1,\"name\":\"test name\",\"price\":9.99,\"description\":\"test description\",\"quantity\":1,\"createdAt\":null,\"updatedAt\":null}");
    }
}
