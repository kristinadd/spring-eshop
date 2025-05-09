package com.kristina.ecom.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortByPrice implements SortStrategy {

  @Override 
  public void sort(List<Computer> cart) {
    Comparator<Computer> comparator = new Comparator<>() {
      @Override 
      public int compare(Computer c1, Computer c2) {
        return (int) (c2.getPrice() - c1.getPrice());
      }
    };
    Collections.sort(cart, comparator);
  }
}
