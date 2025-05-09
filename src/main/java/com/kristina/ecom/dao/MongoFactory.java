package com.kristina.ecom.dao;

import com.kristina.ecom.domain.ShoppingCart;

public class MongoFactory<K, V> implements AbstractFactory<K, V> {
  private static MongoFactory<String, ShoppingCart> instance = new MongoFactory();

  private MongoFactory() {}

  public static MongoFactory<String, ShoppingCart> getInstance() {
    return instance;
  }

  public DAO create(DAO.Type type) {
    return new ShoppingCartDAOMongo();
  }
}
