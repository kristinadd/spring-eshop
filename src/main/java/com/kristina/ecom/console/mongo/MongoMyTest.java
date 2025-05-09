package com.kristina.ecom.console.mongo;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bson.Document; // This class is part of the MongoDB BSON (Binary JSON) library and is used to represent MongoDB documents in Java.
import org.bson.conversions.Bson;

import com.kristina.ecom.domain.Order;
import com.kristina.ecom.domain.Product;
import com.mongodb.BasicDBObject; // It’s used to create a query object.
import com.mongodb.MongoException;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoClient; // MongoDB JAVA Driver --> The main interface for connecting to MongoDB.
import com.mongodb.client.MongoClients; // MongoDB JAVA Driver --> A factory class to create MongoClient instances.
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase; // Represents MongoDB database
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.DeleteOneModel;

public class MongoMyTest {
  public static void main(String[] args) {
    // DATABASE CONNECTION
    MongoClient mongo = MongoClients.create("mongodb://127.0.1:27017"); // This creates a connection to a MongoDB instance running on 127.0.1:27017.
    // Establishes a connection to MongoDB running on 127.0.1:27017
    // This connection is open but not closed; it should be closed when done
    MongoDatabase database  = mongo.getDatabase("ecom");
    // This retrieves the database named "ecom"
    // If the "ecom" database does not exist, MongoDB does not create it 
    // immediately; it will be created only when data is added.
    System.out.println(database.getName());
    database.listCollectionNames().forEach(System.out::println);    // database.listCollectionNames().forEach(collection -> System.out.println(collection));

    System.out.println("----------------------------------------------DATABSE--STATISTICS------------------------------");
    Document database_statistics = database.runCommand(new Document("dbstats", 1));     // runs a MongoDB command and returns a Document object.
    for(Map.Entry<String, Object> entry : database_statistics.entrySet()) {
      System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
    }

    System.out.println("---------------------------------------RETRIEVE--COLLECTIONS--FROM--DATABSE----------------------");
    MongoCollection<Document> collection = database.getCollection("products");
    // retrieve collection of records
    MongoCursor<Document> cursor = collection.find().iterator();
    while (cursor.hasNext()) {
      Document document = cursor.next();
      System.out.println(document);

      System.out.println("-----------------------------------------------RETRIEVE--ONLY--VALUES--FROM--DOCUMENT-------------------------------");
      ArrayList<Object> product_values = new ArrayList<>(document.values()); // values() method returns Collection<Object>
      System.out.println(product_values);
      System.out.printf("Product values: %s, %s\n", product_values.get(0), product_values.get(1));
    }

      System.out.println("-------------------------------CONSTRUCT--QUERY--USING--BASIC-DB-OBJECT----------------------------------------");
      BasicDBObject query = new BasicDBObject("type", new BasicDBObject("$eq", "Component"))
                              .append("price", new BasicDBObject("$eq", 2.99));

      collection.find(query).forEach((Consumer<Document>) document -> {
        System.out.println(document.toJson());
      });


    System.out.println("🟡---------------------------------CRUD--OPERATIONS--FOR--PRODUCT-------------------------------🟡");
    // set database and collection
    database = mongo.getDatabase("ecom");
    collection = database.getCollection("products");

    System.out.println("🟡------------------------------------CREATE--ONLY--1--PRODUCT----------------------------------🟡");
    // create Java object and convert it to document
    Product product = new Product(1, "Component", "Keyboard", 65.99, 15, "img");
    Document document = createProductDocument(product);

    // write to the database
    try {
      InsertOneResult result = collection.insertOne(document);
      System.out.println("Successfully inserted: " + result.getInsertedId() + " class: " + result.getClass());
    } catch (MongoException e) {
      // e.printStackTrace();
      System.out.println("🔴");
    }

    System.out.println("🟡------------------------------------CREATE--(1+)--PRODUCT-------------------------------------🟡");
    List<Product> products = Arrays.asList(
      new Product(2, "Component", "Headset", 300.99, 10, "img"),
      new Product(3, "Component", "Mouse", 35.99, 5, "img"),
      new Product(4, "Component", "Iphone", 1065.99, 3, "img"),
      new Product(5, "Component", "Monitor", 1500.00, 5, "img"),
      new Product(6, "Component", "MacBook", 2500.00, 7, "img")
    );
    List<Document> documentList = new ArrayList<>();

    for (Product p : products) {
      documentList.add(createProductDocument(p));
    }

    try {
      InsertManyResult result = collection.insertMany(documentList);
      System.out.println("Inserted successfully: " + result.getInsertedIds());
    } catch (MongoException e) {
      // e.printStackTrace();
      System.out.println("🔴");
    }
    System.out.println("🟡-------------------------------------READ--ONLY--1--PRODUCT-----------------------------------🟡");
    Bson fields = Projections.fields(
      Projections.include("type", "name", "price"),
      Projections.excludeId()
    );
    
    Document document2 = collection.find(eq("type", "Component"))
    .projection(fields)
    .sort(Sorts.descending("price"))
    .first();
    if (document2 != null) {
      System.out.println(document2.toJson());
    }

    System.out.println("🟡-------------------------------------READ--(1+)--PRODUCT-----------------------------------🟡");
    Bson fields2 = Projections.fields(
      Projections.include("type", "name", "price"),
      Projections.excludeId()
    );
    MongoCursor<Document> cursor2 = collection.find(lt("price", 1500))
    .projection(fields2)
    .sort(Sorts.ascending("price"))
    .iterator();

    try {
      while (cursor2.hasNext()) {
        System.out.println(cursor2.next().toJson());
      }
    } catch (MongoException e ) {
      System.out.println("🔴");
    }


    System.out.println("🟡-------------------------------------UPDATE--ONLY--1--PRODUCT-----------------------------------🟡");
    Bson updates = Updates.combine(
      Updates.set("quantity", 35),
      Updates.currentTimestamp("lastUpdated")
    );
    UpdateOptions options = new UpdateOptions().upsert(true);
    Document document3 = new Document().append("_id", 3);
    try {
      UpdateResult result = collection.updateOne(document3, updates, options);
      System.out.println("Modified document count: " + result.getModifiedCount());
      System.out.println("Updated id: " + result.getUpsertedId());
    } catch (MongoException e) {
    System.out.println("🔴");
    }

    System.out.println("🟡-------------------------------------UPDATE--(1+)--PRODUCTS-----------------------------------🟡");
    Bson updates2 = Updates.combine(
      Updates.set("quantity", 88),
      Updates.currentTimestamp("lastUpdated")
    );

    Bson query1 = gte("price", 2500);
    try {
      UpdateResult result = collection.updateMany(query1, updates2);
      System.out.println("Modified count: " + result.getModifiedCount());
    } catch (MongoException e ) {
      System.out.println("🔴");
    }

    System.out.println("🟡-------------------------------------REPLACE--(1)--PRODUCTS-----------------------------------🟡");
    Bson query2 = eq("_id", 6);
    Document document4 = new Document()
    .append("age", 100)
    .append("color", "white");

    ReplaceOptions replaceOptions = new ReplaceOptions().upsert(true);

    try {
      UpdateResult result = collection.replaceOne(query2, document4, replaceOptions);
      System.out.println("Replaced document counts: " + result.getModifiedCount());
      System.out.println("Updated id: " + result.getUpsertedId());
    } catch (MongoException e) {
      System.out.println("🔴");
    } finally {
      cursor2.close();
    }

    System.out.println("🟡-------------------------------------DELETE--(1)--PRODUCT-----------------------------------🟡");
    Bson query3 = eq("_id", 0);
    try {
      DeleteResult result = collection.deleteOne(query3);
      System.out.println("Deleted count: " + result.getDeletedCount());
    } catch (MongoException ex) {
      ex.printStackTrace();
    }

    System.out.println("🟡-------------------------------------DELETE--(1+)--PRODUCTS-----------------------------------🟡");
    Bson query4 = lt("price", 1500);
    try {
      DeleteResult result = collection.deleteMany(query4);
      System.out.println("Deleted count: " + result.getDeletedCount());
    } catch (MongoException ex) {
      ex.printStackTrace();
    }

    System.out.println("🟡-------------------------------------BULK--WRITE--PRODUCTS-----------------------------------🟡");
    // create, update, delete , use Model
    // no transaction for the bulk
    try {
      BulkWriteResult result = collection.bulkWrite(
        Arrays.asList(
      new InsertOneModel<>(createProductDocument(products.get(0))),
      new InsertOneModel<>(createProductDocument(products.get(1))),

      new UpdateOneModel<>(createProductDocument(products.get(2)), new Document("$set", new Document("price", 100.99)),new UpdateOptions().upsert(true)),

      new DeleteOneModel<>(createProductDocument(products.get(0)))
      ));
      System.out.println("Inserted count: " + result.getInsertedCount()
      + " Updated Count: " + result.getModifiedCount()
      + " Delete count: " + result.getDeletedCount());
    } catch (MongoException ex) {
      ex.printStackTrace();
    }


    System.out.println("🟡-------------------------------------CREATE--(1)--ORDER-----------------------------------🟡");
    collection = database.getCollection( "orders");

    List<Product> productsList = Arrays.asList(
      new Product(1, "Component", "product1", 6.99, 7, "img"),
      new Product(2, "Component", "product2", 2.99, 7, "img"),
      new Product(3, "Component", "product3", 22.99, 7, "img")
    );

    Order order = new Order("First order", LocalDateTime.now(), productsList); // need to specify float

    Document orderDocument = new Document();
    orderDocument = createOrderDocument(order);
    
    try {
      InsertOneResult result = collection.insertOne(orderDocument);
      System.out.println("Inserted order document with id: " + result.getInsertedId());
    } catch (MongoException e ) {
      // e.printStackTrace();
      System.out.println("🔴");
    }

    System.out.println("🟡-------------------------------------READ--(1)--ORDER-----------------------------------🟡");
    Bson orderFields = Projections.fields(
      Projections.include("description", "price", "date", "products")
    );
    
    Document orderDocument2 = collection.find(eq("_id", "1"))
    .projection(orderFields)
    .first();
    System.out.println(orderDocument2.toJson());
  }

  ////////////////////////////////////////////////////////////METHODS////////////////////////////////////////////////////////////////////////
  private static Document createProductDocument(Product product) {
    Document document = new Document();

    document.append("_id", product.getId());
    document.append("type", product.getType());
    document.append("name", product.getName());
    document.append("price", product.getPrice());
    document.append("quantity", product.getQuantity());
    document.append("image", product.getImg());

    return document;
  }

    private static Document createOrderDocument(Order order) {
    Document document = new Document();

    document.append("_id", order.getId());
    document.append("description", order.getDescription());
    document.append("price", order.getTotal());
    document.append("date", order.getDate());

    // needs to be a list of documents
    List<Document> productDocs = new ArrayList<>();
    for (Product product : order.getProducts())
      productDocs.add(createProductDocument(product));
    //

    document.append("products", productDocs);

    return document;
  }
}
