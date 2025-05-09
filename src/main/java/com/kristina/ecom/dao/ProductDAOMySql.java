package com.kristina.ecom.dao;

import javax.sql.DataSource;

import com.kristina.ecom.domain.Product;

import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;

public class ProductDAOMySql implements DAO<Integer, Product> {

  private DataSource datasource;

  public ProductDAOMySql() {
    this.datasource = DataSourceFactory.instance().getDataSource();
  }

  @Override
  public Product create(Product product) throws DAOException {
    
    try {
    Connection conn = datasource.getConnection();
    String query = "INSERT INTO product (type, name, price, quantity, image) VALUES(? ,?, ?, ?, ?)";

    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, product.getType());
    stat.setString(2, product.getName());
    stat.setDouble(3, product.getPrice());
    stat.setInt(4, product.getQuantity());
    stat.setString(5, product.getImg());
    stat.executeUpdate();

    conn.close();
  } catch (SQLException ex) {
    throw new DAOException("Error in DAO", ex);
  }
    return product;
  }

  @Override
  public List<Product> readAll() throws DAOException {
    List<Product> products = new ArrayList<>();

    try {
    Connection conn = datasource.getConnection();
    String query = "SELECT * FROM product";
    Statement stat = conn.createStatement();
    ResultSet rs = stat.executeQuery(query);
      while (rs.next()) {
        Product product = new Product(rs.getInt(1), 
                                      rs.getString(2), 
                                      rs.getString(3), 
                                      rs.getDouble(4), 
                                      rs.getInt(5), 
                                      rs.getString(6)
                                      );
        products.add(product);
      }

      conn.close();
    } catch (SQLException ex) {
      throw new DAOException("Error in DAO", ex);
    }
      return products;
  }

  @Override
  public Product read(Integer id) throws DAOException {
    Product product = null;
    try {
    String query = "SELECT * FROM product WHERE id=" + id;
    Connection conn = datasource.getConnection();
    Statement stat = conn.createStatement();
    ResultSet rs = stat.executeQuery(query);
    if (rs.next())
      product = new Product(rs.getInt(1), 
                            rs.getString(2), 
                            rs.getString(3), 
                            rs.getDouble(4), 
                            rs.getInt(5), 
                            rs.getString(6)
                            );
    return product;
    } catch (SQLException ex) {
      throw new DAOException("Error in DAO", ex);
    }
  }

  @Override
  public int update(Product product) throws DAOException {
    String query = "UPDATE product SET type=?, name=?, price=?, quantity=?, image=? WHERE id=?";
    int rows = 0;
    try {
    Connection conn = datasource.getConnection();
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1,product.getType());
    stat.setString(2,product.getName());
    stat.setDouble(3, product.getPrice());
    stat.setInt(4, product.getQuantity());
    stat.setString(5, product.getImg());
    stat.setInt(6, product.getId());
    rows = stat.executeUpdate();

    conn.close();
    } catch (SQLException ex) {
      throw new DAOException("Error in DAO", ex);
    }
    return rows;
  }

  @Override
  public int delete(Integer id) throws DAOException {
    String query = "DELETE FROM product WHERE id=" + id;
    int rows = 0;
    try {
    Connection conn = datasource.getConnection();
    Statement stat = conn.createStatement();
    rows = stat.executeUpdate(query);

    conn.close();
    } catch (SQLException ex) {
      throw new DAOException("Error in DAO", ex);
    }
    return rows;
  }
}
