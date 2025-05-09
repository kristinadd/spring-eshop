package com.kristina.ecom.service;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;

import com.kristina.ecom.dao.DAO;
import com.kristina.ecom.dao.DAOException;
import com.kristina.ecom.dao.DAOFactory;
import com.kristina.ecom.domain.ComputerBase;
import com.kristina.ecom.domain.Order;
import com.kristina.ecom.domain.Product;

public class OrderService {
  private DAO<String, Order> dao; // interface
  private DAO<Integer, Product> daoP;

  public OrderService() {
    dao = DAOFactory.getInstance().create(DAO.Type.ORDER_DAO);
    daoP = DAOFactory.getInstance().create(DAO.Type.PRODUCT_DAO);
  }

  public Order create(Order order) throws DAOException {
    if (new ComputerBase().getBase().getQuantity() <= 0 ) {
      throw new DAOException("Insufficient stock for base computer "  , new Exception());
    }
    
    for (Product product : order.getProducts()) {
      Product stock = daoP.read(product.getId());
      if (stock.getQuantity() < product.getQuantity()) {
        throw new DAOException("Insufficient stock for product: " + product.getName(), new Exception());
      }
    }

    for (Product product : order.getProducts()) {
      Product stock = daoP.read(product.getId());
      stock.setQuantity(stock.getQuantity() - product.getQuantity());
      daoP.update(stock);
    }
    
    return dao.create(order);
  }

  public List<Order> getAll() {
    List<Order> orders = new ArrayList<>();
    try {
      orders = dao.readAll();
    } catch (DAOException ex) {
      ex.printStackTrace();
    }

    return orders;
  }

  public Order get(String id) {
    Order order = null;
    try {
      order = dao.read(id);
    } catch (DAOException ex) {
      ex.printStackTrace();
    }

    return order;
  }

  public int delete(String id) {
    int rows = 0;
    try {
      rows = dao.delete(id);
    } catch (DAOException ex) {
      ex.printStackTrace();
    }
    return rows;
  }

  public int cancel(String id) {
    int rows = 0;
    try {
      ProductService pService  = new ProductService();
      dao.read(id).getProducts().forEach(p -> {
        Product product = pService.get(p.getId());
        product.setQuantity(product.getQuantity() + p.getQuantity());
        pService.update(product);
      });

      rows = dao.delete(id);
    } catch ( DAOException ex) {
      System.out.println("Error cancelling the order");
    }
    return rows;
  }

  public boolean update(Order order) {
    try {
        Order oldOrder = dao.read(order.getId());
        Product  productFromStock;
        int difference;
        List<Product> oldProducts = oldOrder.getProducts();
        List<Product> newOrderProducts = order.getProducts();
        List<Product> commonProducts = newOrderProducts.stream().filter(p -> oldProducts.contains(p)).collect(Collectors.toList());
        List<Product> onlyOldOrder = oldProducts.stream().filter(p -> !newOrderProducts.contains(p)).collect(Collectors.toList());
        List<Product> onlyNewOrder = newOrderProducts.stream().filter(p -> !oldProducts.contains(p)).collect(Collectors.toList());
        // update existing product or add new product 
        commonProducts.addAll(onlyNewOrder);
        for (Product product : commonProducts) {
          difference = product.getQuantity() - getProductQuantityById(oldProducts, product.getId());
          productFromStock = daoP.read(product.getId());
          productFromStock.setQuantity(productFromStock.getQuantity() - difference);
          daoP.update(productFromStock);
        }
        // delete product from order, increase the stock
        for (Product product : onlyOldOrder) {
          productFromStock = daoP.read(product.getId());
          productFromStock.setQuantity(productFromStock.getQuantity() + product.getQuantity());
          daoP.update(productFromStock);
          }

      dao.update(order);
      return true;
    } catch (DAOException ex) {
      ex.printStackTrace();
      return false;
    }
  }

  // get the product quantity by its id in the oder 
  private int getProductQuantityById(List<Product> products, int id){
    for (Product p : products) 
      if (p.getId()==id)
        return p.getQuantity();

    return 0; 
  } 
}
