package com.kristina.ecom.domain;
public class Component extends ComputerDecorator {
  private String description;
  private double price;

  public Component(Computer computer) {
    super(computer);
  }

  public Component(Computer computer, Product product) {
    super(computer);
    this.description = product.getName();
    this.price = product.getPrice();
    if (super.getComponents().contains(product)) {
      Product p = super.getComponents().get(super.getComponents().indexOf(product));
       p.setQuantity(p.getQuantity() + product.getQuantity());
    } else
      super.getComponents().add(product);
  }

  @Override
  public String getDescription() {
    return super.getDescription() + " + " + this.description;
  }

  @Override
  public double getPrice() {
    return super.getPrice() + this.price;
  }

  @Override
  public String toString() {
    return String.format(
      "🧩 Component\n order_id: %s\n description: %s\n price: %s\n",
      this.getId(), this.getDescription(), this.getPrice()
    );
  } 
}
