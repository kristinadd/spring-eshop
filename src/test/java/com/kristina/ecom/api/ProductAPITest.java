package com.kristina.ecom.api;

import com.kristina.ecom.domain.Product;
import com.kristina.ecom.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(ProductAPI.class)
public class ProductAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setName("Test Product");
        testProduct.setPrice(99.99);
        testProduct.setType("Component");
        testProduct.setQuantity(10);

        // Set up mock behavior
        when(productService.getAll()).thenReturn(Arrays.asList(testProduct));
        when(productService.get(1)).thenReturn(testProduct);
    }

    @Test
    void getAllProducts_ShouldReturnProductList() throws Exception {
        mockMvc.perform(get("/ecom/product/getall"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Product")));
    }

    @Test
    void getProductById_ShouldReturnProduct() throws Exception {
        mockMvc.perform(get("/ecom/product/get/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Product")));
    }

    @Test
    void createProduct_ShouldReturnProductId() throws Exception {
        when(productService.create(testProduct)).thenReturn(1);

        mockMvc.perform(post("/ecom/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk());
    }
} 