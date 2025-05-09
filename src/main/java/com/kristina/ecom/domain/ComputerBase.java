package com.kristina.ecom.domain;
import java.util.stream.Collectors;
import com.kristina.ecom.service.ProductService;
import java.util.List;
import java.util.ArrayList;

public class ComputerBase implements Computer {
  private int id;
  private String description;
  private double price;
  private Product computer;
  private List<Product> components;

  public ComputerBase() {
    // constructor chaining
    this(new ArrayList<Product>());
  }

  public ComputerBase(List<Product> components) {
    computer = new ProductService().getComputer();
    this.id = computer.getId();
    this.components = components;
    update();
  }

  // constructor for MongoDB
  public ComputerBase(int id, List<Product> components) {
    computer = new ProductService().getComputer();
    this.id = id;
    this.components = components;
    update();
  }

  // construct the description and price dynamically
  public void update() {
    description = computer.getName();
    price = computer.getPrice();

    for (Product product : components) {
      description += (" + " + product.getName()).repeat(product.getQuantity());
      price += product.getPrice() * product.getQuantity();
    }
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public double getPrice() {
    return this.price;
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public List<Product> getComponents() {
    return components;
  }

  @Override
  public Product getBase() {
    return computer;
  }

  @Override
  public void setComponents(List<Product> components) {
    this.components = components;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public String toString() {
      // Convert the list of components (Product objects) into a single string.
      // This will call each Product's toString() method.
      String componentsString = components.stream()
                                      .map(Product::toString)
                                      .collect(Collectors.joining(", "));
      
      return String.format(
          "\n🖥️ ComputerBase:\n id: %s\n description: %s\n price: %.2f\n components: [%s]",
          id, description, price, componentsString
      );
  }
}
