package com.joeyharbert.ecommerce.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.joeyharbert.ecommerce.business.ProductsService;
import com.joeyharbert.ecommerce.data.Image;
import com.joeyharbert.ecommerce.data.Product;
import com.joeyharbert.ecommerce.data.ProductRepository;
import com.joeyharbert.ecommerce.data.Supplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    Supplier testSupplier;

    Image testImage;
    long supplierId;

    String productString;

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
        testImage = new Image();
        testImage.setUrl("test.jpg");
        Set<Image> images = new HashSet<>();
        images.add(testImage);
        testProduct.setImages(images);
        productString = "{\"id\":1,\"name\":\"test name\",\"price\":9.99,\"description\":\"test description\",\"quantity\":1,\"createdAt\":null,\"updatedAt\":null,\"supplier\":{\"id\":1,\"name\":\"test name\",\"email\":\"test@test.com\",\"phoneNumber\":\"555-555-5555\",\"createdAt\":null,\"updatedAt\":null},\"images\":[{\"id\":0,\"url\":\"test.jpg\",\"createdAt\":null,\"updatedAt\":null}]}";
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
        assertThat(response).isEqualTo("[" + productString + "]");
    }

    @Test
    public void givenProduct_whenGetProduct_thenStatus200() throws Exception {
        when(productsService.getProductById(anyLong())).thenReturn(testProduct);

        MvcResult result = this.mockMvc.perform(get("/products/{id}", id)).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertThat(response).isEqualTo(productString);
    }

    @Test public void givenBadId_whenGetProduct_thenStatus404() throws Exception {
        when(productsService.getProductById(anyLong())).thenThrow(new RuntimeException());

        this.mockMvc.perform(get("/products/{id}", id)).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenProduct_whenAddProduct_thenStatus201() throws Exception {
        when(productsService.addProduct(anyMap())).thenReturn(testProduct);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(testProduct);
        MvcResult result = this.mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON).content(json)).andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertThat(response).isEqualTo(productString);
    }

    @Test
    public void givenNoBody_whenAddProduct_thenStatus400() throws Exception {
        this.mockMvc.perform(post("/products")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenBadSupplier_whenAddProduct_thenStatus400() throws Exception {
        when(productsService.addProduct(anyMap())).thenThrow(new RuntimeException());

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(testProduct);
        this.mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON).content(json)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenUpdatedProduct_whenUpdateProduct_thenStatus200() throws Exception {
        //happy path
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "updated name");

        when(productsService.updateProduct(updates, id)).thenReturn(testProduct);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(updates);
        MvcResult result = this.mockMvc.perform(patch("/products/{id}", id).contentType(MediaType.APPLICATION_JSON).content(json)).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertThat(response).isEqualTo(productString);
    }

    @Test
    public void givenNoBody_whenUpdateProduct_thenStatus400() throws Exception {
        this.mockMvc.perform(patch("/products/{id}", id)).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void givenBadId_whenUpdateProduct_thenStatus404() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "updated name");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(updates);
        when(productsService.updateProduct(anyMap(), anyLong())).thenThrow(new RuntimeException());
        this.mockMvc.perform(patch("/products/{id}", 2L).contentType(MediaType.APPLICATION_JSON).content(json)).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void givenProductId_whenDestroyProduct_thenStatus200() throws Exception {
        this.mockMvc.perform(delete("/products/{id}", id)).andDo(print()).andExpect(status().isOk());
        verify(productsService, times(1)).destroyProduct(id);
    }

    @Test
    public void givenBadProductId_whenDestroyProduct_thenStatus404() throws Exception {
        doThrow(new RuntimeException()).when(productsService).destroyProduct(id);

        this.mockMvc.perform(delete("/products/{id}", id)).andDo(print()).andExpect(status().isNotFound());

    }
}
