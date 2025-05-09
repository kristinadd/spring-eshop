package com.kristina.ecom;

import org.junit.jupiter.api.Test;

import com.kristina.ecom.domain.Product;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
  
  @Test
  void testDefaultConstructor() {
    Product product = new Product();
    assertEquals(0, product.getId(), "Default ID should be 0"); 
    assertEquals(0, product.getQuantity(), "Default quantity should be 0");
    assertNull(product.getName(), "Default name should be null");
  }

  @Test
  void testParameterizedConstructor() {
    Product product = new Product(1, "Component", "Phone", 599.99, 10, "phone.jpg");
    assertEquals(1, product.getId(), "ID should match constructor input");
    assertEquals("Component", product.getType(), "Type should match constructor input");
    assertEquals("Phone", product.getName(), "Name should match constructor input");
    assertEquals(599.99, product.getPrice(), "Price should match constructor input");
    assertEquals(10, product.getQuantity(), "Quantity should match constructor input");
    assertEquals("phone.jpg", product.getImg(), "Image should match constructor input");
  }

  @Test
  void testGetterAndSetter() {
    Product product = new Product();
    product.setType("Component");
    product.setName("Headphones");
    product.setPrice(500.99);
    product.setQuantity(15);

    assertEquals("Component", product.getType(), "Type should match setter input");
    assertEquals("Headphones", product.getName(), "Name should match setter input");
    assertEquals(500.99, product.getPrice(), "Price should match setter input");
    assertEquals(15, product.getQuantity(), "Quantity should match setter input");
  }
}
