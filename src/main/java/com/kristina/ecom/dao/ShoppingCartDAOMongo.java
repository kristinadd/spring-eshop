package com.kristina.ecom.dao;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.kristina.ecom.domain.ShoppingCart;
import com.kristina.ecom.domain.Status;
import com.kristina.ecom.domain.Computer;
import com.kristina.ecom.domain.ComputerBase;
import com.kristina.ecom.domain.Product;
public class ShoppingCartDAOMongo  implements DAO<String, ShoppingCart> {
  private MongoDataSourceFactory dataSourceFactory;
  private MongoCollection<Document> collection;

  public ShoppingCartDAOMongo () {
    this.dataSourceFactory = MongoDataSourceFactory.getInstance();
    this.collection = dataSourceFactory.getDatabase().getCollection("shoppingcart");
  }

  @Override
  public ShoppingCart create(ShoppingCart shoppingCart) throws DAOException {
    try {
      Document document = toShoppingDocument(shoppingCart);
      InsertOneResult result = collection.insertOne(document);
      System.out.println(result.getInsertedId());
      // shoppingCart.setId(result.getInsertedId().toString());
      // taking a valid _id, and converting it into a string that is no longer a valid ObjectId hex, 
      // and saving that in the Java object — which is then used in update() later

      // solution
      ObjectId insertedId = result.getInsertedId().asObjectId().getValue();
      shoppingCart.setId(insertedId.toHexString());

      return shoppingCart;
    } catch (MongoException ex) {
      throw new DAOException("❌ Coudn't create the shopping cart", ex);
    }
  }

  @Override 
  public List<ShoppingCart> readAll() throws DAOException {
    FindIterable<Document> cartDocuments = collection.find();
    List<ShoppingCart> carts = new ArrayList<>();
    for (Document document : cartDocuments) {
      ShoppingCart cart = toShoppingCart(document);
      carts.add(cart);
    }
    return carts;
  }

  @Override // read only the artive one 
  public ShoppingCart read(String user_id) throws DAOException {
      Document shoppingDocument = collection.find(and(
        eq("user_id", user_id), 
        eq("status", Status.ACTIVE.toString()))).first();
    if (shoppingDocument == null) {
      return null;
    }
    return toShoppingCart(shoppingDocument);
  }

  public ShoppingCart read(Status status) throws DAOException {
    Document shoppingDocument = collection.find(eq("status", status)).first();
    if (shoppingDocument == null) {
      return null;
    }
    return toShoppingCart(shoppingDocument);
  }

  public ShoppingCart readId(String id) throws DAOException {
    if (id == null) {
      return null;
    }
    Bson query = eq("_id", new ObjectId(id));
    Document document = collection.find(query).first();

    if (document != null) {
      ShoppingCart cart = toShoppingCart(document);
      return cart;
    }

    return null;
  }

  @Override
  public int update(ShoppingCart shoppingCart) throws DAOException {
    Bson query = eq("_id", new ObjectId(shoppingCart.getId()));
    ReplaceOptions replaceOptions = new ReplaceOptions().upsert(false);
    try {
    collection.replaceOne(query, toShoppingDocument(shoppingCart), replaceOptions);
    return 1;
    } catch (MongoException ex) {
      throw new DAOException("Failed to update the Shopping Cart", ex);
    }
  }

  @Override // delete the entire shopping cart
  public int delete(String id) throws DAOException {
    Document shopDocument = collection.find(eq("_id", new ObjectId(id))).first();
    if (shopDocument != null) {
      try {
        DeleteResult result = collection.deleteOne(shopDocument);
        System.out.println("❌ Deleted: " + result.getDeletedCount());
        return (int) result.getDeletedCount();
      } catch (MongoException ex) {
        throw new DAOException("❌ Coudn't delete the shopping cart", ex);
      }
    }
    return 0;
  }

  // from object to document
    private Document toShoppingDocument(ShoppingCart shoppingCart) throws DAOException {
    Document document = new Document();

    // document.append("_id", shoppingCart.getId());
    document.append("user_id", shoppingCart.getUserId());
    document.append("updated_at", shoppingCart.getUpdatedAt());
    document.append("status", shoppingCart.getStatus());

    List<Document> computerDocuments = new ArrayList<>();
    List<Computer> computers = shoppingCart.getComputers();
      for (Computer computer : computers) {
        Document computerDoc = toComputerDocument(computer);
        computerDocuments.add(computerDoc);
      }

    document.append("computers", computerDocuments);
    return document;
  }

  private Document toComputerDocument(Computer computer) {
    Document computerDoc = new Document();
    computerDoc.append("_id", computer.getId());
    computerDoc.append("description", computer.getDescription());
    computerDoc.append("price", computer.getPrice());
    computerDoc.append("products", toProductDocuments(computer.getComponents())); // many products

    return computerDoc;
  }

  private List<Document> toProductDocuments(List<Product> products) {
    List<Document>  productDocuments = new ArrayList<>();

    for (Product product : products) {
      Document productDoc = UtilDAOMongo.toDocument(product);      // use UtilMongo instead of calling the productDAO directly
      
      productDocuments.add(productDoc);
    }
    return productDocuments;
  }

  // from document to object
  private Computer toComputer(Document document) {
    Document computers = document.get("computers", Document.class); // get the nested object first

    List<Document> productDocuments = computers.getList("products", Document.class);
    List<Product> products = toProducts(productDocuments);

    Computer computer = new ComputerBase(
      computers.getInteger("_id"),
      products
    );

    return computer;
  }

  private List<Product> toProducts(List<Document> productDocuments) {
    List<Product> products = new ArrayList<>();

    for (Document productDoc : productDocuments) {
      Product product = UtilDAOMongo.toProduct(productDoc);
      products.add(product);
    }
    return products;
  }

  private ShoppingCart toShoppingCart(Document document) {
    List<Computer> computers = new ArrayList<>();
    List<Product> products = new ArrayList<>();

    List<Document> computerDocuments = new ArrayList<>();
    
    computerDocuments = document.getList("computers", Document.class);
    if (computerDocuments != null) {
      for (Document doc : computerDocuments) {
        products = new ArrayList<>(); // needs to use a new list 
        
        List<Document> productDocs = doc.getList("products", Document.class);
        for (Document proDoc : productDocs) {
          Product product = new Product(
            proDoc.getInteger("_id"),
            proDoc.getString("type"),
            proDoc.getString("name"),
            proDoc.getDouble("price"),
            proDoc.getInteger("quantity"),
            proDoc.getString("image")
          );
          products.add(product);
        }

        Computer computer = new ComputerBase(
          doc.getInteger("_id"),
          products
        );
        computers.add(computer);
      }
    } else {
      System.out.println("❌ computerDocuments is null");
    }

    ShoppingCart cart = new ShoppingCart(
    document.getObjectId("_id").toString(),
    document.getString("user_id"),
    document.getDate("updated_at"),
    Status.valueOf(document.getString("status")),
    computers
    );

    return cart;
  }
}
