package com.kristina.ecom.domain;

public class Product implements Cloneable {
  private int id;
  private String type;
  private String name;
  private double price;
  private String img;
  private int quantity;

  public Product() {
    // default constructor
  }

  public Product(String type, String name, double price, String img) {
    this(0, type, name, price, 0, img);
  }

  public Product(String type, String name, double price, int quantity) {
    this(0, type, name, price, quantity,"");
  }


  public Product(String type, String name, double price, int quantity, String img) {
    this(0, type, name, price, quantity, img);
  }

  public Product( int id, String name, double price, int quantity) {
    this(id,  "Component", name, price, quantity, "default.jpg");
  }

  public Product(int id, String type, String name, double price, int quantity, String img) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.img = img;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPrice() {
    return this.price;
  }

  public void setPrice(double price) {
     this.price = price;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
 }

  public String getImg() {
    return this.img;
  }

  public void setImg(String image) {
    this.img = image;
  }

  @Override
  public String toString() {
      return String.format(
          "📍Product:\n" +
          "  id: %d\n" +
          "  type: %s\n" +
          "  name: %s\n" +
          "  price: %.2f\n" +
          "  quantity: %d\n" +
          "  img: %s",
          id, type, name, price, quantity, img
      );
  }  
  

  // @Override
  // public Product clone() {
  //   return new Product(this.id, this.name, this.price, this.quantity, this.img);
  // }

  @Override
  public Object clone() throws CloneNotSupportedException { // runtime exception, no need to catch 
    return super.clone();
  }

  @Override 
  public boolean equals(Object obj) {
    if (!(obj instanceof Product))
      return false;

    return ((Product) obj).getId() ==  this.getId();
    // When dealing with generic (parameterized) classes in Java, 
    // the generic type parameters are erased at runtime (a process 
    // called type erasure). This means that you cannot directly 
    // check for a specific generic type (like Product<Integer>) 
    // at runtime. Instead, you can check against the raw type 
    // or use a wildcard.
  };
}
// == compares if two objects have the same reference in memory
// the deffault equals uses == if not iverriden
