package com.joeyharbert.ecommerce.web;

import com.joeyharbert.ecommerce.business.ProductsService;
import com.joeyharbert.ecommerce.data.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductsService productsService;

    @Test
    public void givenProducts_whenGetProducts_thenStatus200() throws Exception {
        when(productsService.getAllProducts()).thenReturn(new ArrayList<Product>());

        this.mockMvc.perform(get("/products")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }
}
