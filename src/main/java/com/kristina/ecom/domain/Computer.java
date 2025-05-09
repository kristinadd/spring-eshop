package com.kristina.ecom.domain;

import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

  @JsonDeserialize(as = ComputerBase.class)
  // serialize as ComputerBase

public interface Computer {
  String getDescription();
  double getPrice();
  int getId();
  Product getBase();
  List<Product> getComponents();
  void setComponents(List<Product> components);
}
