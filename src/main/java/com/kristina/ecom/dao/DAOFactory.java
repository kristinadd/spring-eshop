package com.kristina.ecom.dao;
import java.io.IOException;
import java.util.Properties;

public class DAOFactory implements AbstractFactory {
  private static DAOFactory instance = new DAOFactory();
  private Properties properties;

  private DAOFactory() {
    load("db.properties");
  }

  public static DAOFactory getInstance() {
    return instance;
  }

  public DAO create(DAO.Type type) {
    if (type == null) {
      return null;
    }

    // read the prop file
    if (properties.get(type.name()).equals(DAO.Type.SQL.name())) 
          return SQLFactory.getInstance().create(type);
    else if (properties.get(type.name()).equals(DAO.Type.MONGO.name())) 
          return MongoFactory.getInstance().create(type);

    return null;
  }

  private void load(String fname) {
    properties = new Properties();

    try {
      properties.load(getClass().getClassLoader().getResourceAsStream(fname));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
