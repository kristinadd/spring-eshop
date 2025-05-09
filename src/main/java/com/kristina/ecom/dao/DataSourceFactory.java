package com.kristina.ecom.dao;

import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

public class DataSourceFactory {
  // Singleton pattern, only one instance can be created
  // The static keyword means that the instance variable belongs to the class itself, not to any particular object (instance) of the class.
  private static DataSourceFactory instance = new DataSourceFactory("db.properties");
  private Properties props;

  // Singleton pattern, private constructor, can't be called outside of the class
  private DataSourceFactory(String fname) {
     props = new Properties();
    try {
      props.load(getClass().getClassLoader().getResourceAsStream(fname));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public static DataSourceFactory instance() {
    return instance;
  }

  public DataSource getDataSource() {
    MysqlDataSource datasource = new MysqlDataSource();
      datasource.setURL(props.getProperty(("DB_URL")));
      datasource.setUser(props.getProperty(("DB_USER")));
      datasource.setPassword(props.getProperty(("DB_PASSWORD")));

      return datasource;
  }
}
