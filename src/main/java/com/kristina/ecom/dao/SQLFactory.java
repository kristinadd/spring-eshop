package com.kristina.ecom.dao;

public class SQLFactory implements AbstractFactory {
  private static SQLFactory instance = new SQLFactory();

  private SQLFactory() {}

  public static SQLFactory getInstance() {
    return instance;
  }
  
  public DAO create(DAO.Type type) {
    if (type == null) {
      return null;
    }

    switch (type) {
      case DAO.Type.ORDER_DAO:
        return new OrderDAOMySql();
      case DAO.Type.PRODUCT_DAO:
        return new ProductDAOMySql();
      default:
      throw new IllegalArgumentException("no such type: " + type);
    }
  }
}
