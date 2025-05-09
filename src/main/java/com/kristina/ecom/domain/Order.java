package com.kristina.ecom.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
public class Order {
  private static final int SIZE = 10000;
  private static List<Integer> ids = new Random().ints(1, SIZE+1)
  .distinct().limit(SIZE).boxed().collect((Collectors.toList()));

  private String id;
  private LocalDateTime date;
  private Computer computer;

  public Order() {}


  public Order(Computer computer) {
    this( // chaining
      getID(),
      LocalDateTime.now(),
      computer
    );
  }

  public Order(String id, LocalDateTime date, Computer computer) {
    this.id = id;
    this.date = date;
    this.computer = computer;
  }

  public Order(String id, LocalDateTime date, List<Product> products) {
    this.id = id;
    this.date = date;
    this.computer = new ComputerBase(products);
  }

  //   public Order(Computer computer) {
  //     this( // constructor chainnig
  //     getID(),
  //     computer.getDescription(), 
  //     (float) computer.getPrice(), 
  //     LocalDateTime.now(),
  //     computer.getComponents()
  //     );
  // }
  
  // public Order(String id, String description, float total, LocalDateTime date, List<Product> products) {
  //   this.id = id;
  //   this.description = description;
  //   this.total = total;
  //   this.date = date;
  //   this.products= products;
  // }

  // public Order(String description, float total, LocalDateTime date, List<Product> products) {
  //   this.id = "";
  //   this.description = description;
  //   this.total = total;
  //   this.date = date;
  //   this.products = products;
  // }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDescription() {
    return computer.getDescription();
  }

  public void update() {
    this.setDate(LocalDateTime.now());
  }

  public double getTotal() {
    return computer.getPrice();
  }

  public LocalDateTime getDate() {
    return date;
  }

  public Computer getComputer() {
    return computer;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public List<Product> getProducts() {
    return computer.getComponents();
  }

  public void setProducts(List<Product> products) {
    this.computer.setComponents(products);
  }

  @Override
  public String toString() {
    return String.format("OrderID@%s: %s $%.2f", this.id, computer.getDescription(), computer.getPrice());
  }

  private static String getID() {
    return Integer.toString(ids.remove(0));
  }
}
