package com.kristina.ecom.api;

import com.kristina.ecom.domain.Product;
import com.kristina.ecom.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")  // This will use application-test.properties
public class ProductAPIIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        // Clean up all products before each test
        List<Product> allProducts = productService.getAll();
        for (Product p : allProducts) {
            productService.delete(p.getId());
        }
        // Create and save a test product in the database
        testProduct = new Product();
        testProduct.setName("Integration Test Product");
        testProduct.setPrice(99.99);
        testProduct.setType("Component");
        testProduct.setQuantity(10);
        // Save to database and get the new ID
        int newId = productService.create(testProduct);
        testProduct.setId(newId);
    }

    @Test
    void getAllProducts_ShouldReturnProductList() throws Exception {
        mockMvc.perform(get("/ecom/product/getall"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[?(@.name == 'Integration Test Product')]", hasSize(greaterThan(0))));
    }

    @Test
    void getProductById_ShouldReturnProduct() throws Exception {
        int productId = testProduct.getId();
        mockMvc.perform(get("/ecom/product/get/" + productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.name", is("Integration Test Product")));
    }

    @Test
    void createProduct_ShouldCreateNewProduct() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("New Test Product");
        newProduct.setPrice(149.99);
        newProduct.setType("Component");
        newProduct.setQuantity(5);

        // Create product and get the returned ID
        String response = mockMvc.perform(post("/ecom/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        int newId = Integer.parseInt(response);

        // Fetch the product and verify
        mockMvc.perform(get("/ecom/product/get/" + newId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(newId)))
                .andExpect(jsonPath("$.name", is("New Test Product")));
    }
} 