package com.kristina.ecom.dao;
import java.util.List;

public interface DAO<K, V> {
  V create(V v) throws DAOException;
  List<V> readAll() throws DAOException;
  V read(K k) throws DAOException;
  int update(V v) throws DAOException;
  int delete(K k) throws DAOException;

  public enum Type {
    SQL, PRODUCT_DAO, ORDER_DAO, MONGO, SHOPPING_CART_DAO
  }
}
