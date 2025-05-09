package com.kristina.ecom.dao;

public interface AbstractFactory<K,V> {

  public DAO<K, V> create(DAO.Type dao);
}
