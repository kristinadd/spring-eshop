package com.kristina.ecom.api;
import com.kristina.ecom.domain.Product;
import com.kristina.ecom.service.ProductService;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("ecom/product")
public class ProductAPI {
  private ProductService service;

  public ProductAPI(ProductService service) {
    this.service = service;
  }


  @GetMapping(value="/getall", produces="application/json")
  public List<Product> getAll() {
    return service.getAll();
  }

  @GetMapping(value="/get/{id}", produces="application/json")
  public ResponseEntity<Product> get(@PathVariable int id) {
    Product product = service.get(id);
    if (product == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(product);
  }


  @PostMapping(value="/create", consumes="application/json")
  public int create(@RequestBody Product product) {
    return service.create(product);
  }

  @DeleteMapping(value="/delete/{id}")
  public int delete(@PathVariable int id) {
    return service.delete(id);
  }

  @PutMapping(value="/update", produces="application/json")
  public int update (@RequestBody Product product) {
    return service.update(product);
  }

  @GetMapping(value="/getcomputer", produces="application/json")
  public Product getComputer() {
    return service.getComputer();
  }
}
