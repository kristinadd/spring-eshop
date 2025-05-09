package com.kristina.ecom.domain;

import java.util.List;

public class ComputerDecorator implements Computer {
  private Computer computer;

  public ComputerDecorator(Computer computer) {
    this.computer = computer;
  }

  @Override
  public String getDescription() {
    return this.computer.getDescription();
  }

  @Override
  public double getPrice() {
    return this.computer.getPrice();
  }
  
  @Override
  public int getId() {
    return this.computer.getId();
  }

  @Override
  public Product getBase() {
    return this.computer.getBase();
  }

  @Override
  public List<Product> getComponents() {
    return this.computer.getComponents();
  }

  @Override
  public void setComponents(List<Product> components) {
    this.computer.setComponents(components);
  }

  @Override
  public String toString() {
    return "ComputerDecorator [computer=" + computer + ", getDescription()=" + getDescription() + ", getPrice()="
        + getPrice() + ", getId()=" + getId() + ", getComponents()=" + getComponents() + "]";
  }

  public Computer getComputer() {
    return computer;
  }
}
