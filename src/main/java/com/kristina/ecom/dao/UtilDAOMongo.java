package com.kristina.ecom.dao;

import org.bson.Document;

import com.kristina.ecom.domain.Product;

public class UtilDAOMongo {

  public static Product toProduct(Document document) {
    if (document == null)
      return null;

    Product product = new Product(
      // document.getObjectId("_id").toString(),
      document.getInteger("_id"),
      document.getString("type"),
      document.getString("name"),
      document.getDouble("price"),
      document.getInteger("quantity"),
      document.getString("image")
      );
    return product;
  }

  public static Document toDocument(Product product) {
  if (product != null) {
  Document document = new Document();
    if (product.getId() != 0)
      document.append("_id", product.getId());
      document.append("type", product.getType());
      document.append("name", product.getName());
      document.append("price", product.getPrice());
      document.append("quantity", product.getQuantity());
      document.append("image", product.getImg());

    return document;
  } else {
    System.out.println("Can't convert to document. Product is either null or invalid.");
  }
  return null;
  }
}
