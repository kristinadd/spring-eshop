package com.kristina.ecom.dao;

public class DAOException extends Exception {
  public DAOException(String errMsg, Exception ex) {
    super(errMsg, ex);
  }
}
