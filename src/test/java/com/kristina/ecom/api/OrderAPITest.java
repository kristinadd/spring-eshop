package com.kristina.ecom.api;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kristina.ecom.domain.Order;
import com.kristina.ecom.service.OrderService;

@WebMvcTest(OrderAPI.class)
public class OrderAPITest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private OrderService orderService;

  @Autowired
  private ObjectMapper objectMapper;

  private Order order;

  @BeforeEach
  void setUp() {
    order = new Order();
  }
}
