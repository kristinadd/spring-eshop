package com.kristina.ecom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kristina.ecom.dao.DAO;
import com.kristina.ecom.dao.DAOException;
import com.kristina.ecom.dao.DAOFactory;
import com.kristina.ecom.domain.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
  private DAO<String, Product> dao;

  public ProductService() {
    dao = DAOFactory.getInstance().create(DAO.Type.PRODUCT_DAO);
  }

  public List<Product> getAll() {
    List<Product> products = new ArrayList<>();
    try {
      products = dao.readAll()
      .stream()
      .filter(product -> product.getType() != null && product.getType().equals("Component"))
      .collect(Collectors.toList());

    } catch (DAOException ex) {
      ex.printStackTrace();
    }

    return products;
  }

  public Product getComputer() {
    Product product = null;
    try {
      product = dao.readAll().stream()
            .filter(p -> p.getType() != null && p.getType().equals("Compute"))
            .findFirst().orElse(null);
    } catch (DAOException ex) {
        ex.printStackTrace();
    }

    return product;
  }

  public Product get(int id) {
    Product product = null;
    try {
      product = dao.read(String.valueOf(id));
    } catch (DAOException ex) {
      ex.printStackTrace();
    }

    return product;
  }

  public int delete(int id) {
    int rows = 0;
    try {
      rows = dao.delete(String.valueOf(id));
    } catch (DAOException ex) {
      ex.printStackTrace();
    }
    return rows;
  }

  public int create(Product product) {
    try {
      Product createdProduct = dao.create(product);
      return createdProduct != null ? 1 : 0;
    } catch (DAOException ex) {
      ex.printStackTrace();
      return 0;
    }
  }

  public int update(Product product) {
    int rows = 0;
    try {
      rows = dao.update(product);
    } catch (DAOException ex) {
      ex.printStackTrace();
    }

    return rows;
  }
}
