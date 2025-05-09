package com.kristina.ecom.domain;

import java.util.List;
public interface Computer {
  String getDescription();
  double getPrice();
  int getId();
  Product getBase();
  List<Product> getComponents();
  void setComponents(List<Product> components);
}
