package com.kristina.ecom.api;

import com.kristina.ecom.domain.Order;
import com.kristina.ecom.service.OrderService;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("ecom/order")
public class OrderAPI {
  private OrderService service;

  // run spring
  // ./mvnw spring-boot:run

  // Manual 
  public OrderAPI() {
    service = new OrderService();
  }

  // Constructor-based Dependency Injection
  // public OrderAPI(OrderService service) {
  //   this.service = service;
  // }

  @GetMapping(value="/getall", produces="application/json") 
  public List<Order> getAll() {
    return service.getAll();
  }

  @GetMapping(value="/get/{id}", produces="application/json")
  public Order get(@PathVariable String id) {
    return service.get(id);
  }

  @PostMapping(value="/create", consumes="application/json")
  public Order create(@RequestBody Order order) {
    try {
    return service.create(order);
    } catch (Exception e) {
      return null; // not ideal, fix later
    }
  }

  @DeleteMapping(value="/delete/{id}")  // don't need to produce json at all
  public int delete(@PathVariable String id) {
    return service.delete(id);
  }

  @PutMapping(value="/update", produces="application/json")
  public boolean update (@RequestBody Order order) {
    return service.update(order);
  }

  @DeleteMapping(value="/cancel/{id}") // put / as it's not really a delete, it's updating the memory
  public int cancel(@PathVariable String id) {
    return service.cancel(id);
  }
}
