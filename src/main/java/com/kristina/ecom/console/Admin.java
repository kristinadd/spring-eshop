package com.kristina.ecom.console;

import java.util.Arrays;
import java.util.Scanner;
import com.kristina.ecom.domain.Product;
import com.kristina.ecom.service.ProductService;

public class Admin {
  private static Admin instance = new Admin();
  private Scanner sc;
  private ProductService service;

  private Admin() {
    sc = new Scanner(System.in);
    service = new ProductService();
  }

  public static Admin instance() {
    return instance;
  }

  public void admin() {
    while (true) {
      menu();
      int c = sc.nextInt();
      switch (c) {
        case 1:
          create();
          break;
        case 2:
          read();
          break;
          case 3:
          update();
          break;
        case 4:
          delete();
          break;
        case 5:
          all();
          break;
        case 6:
          return;
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }
  }

  private void menu() {
    String[] adminMenu = {
      "1: Create",
      "2: Read",
      "3: Update",
      "4: Delete",
      "5: All products",
      "6: Return to main menu"
    };
    
    System.out.println("\n*** Product Admin ***");
    Arrays.stream(adminMenu).forEach(System.out::println);
  }

  public void all() {
      Arrays.stream(service.getAll().toArray()).forEach(
        product -> System.out.println(((Product)product).getId() + ":" + product));
    // When dealing with generic (parameterized) classes in Java, 
    // the generic type parameters are erased at runtime (a process 
    // called type erasure). This means that you cannot directly 
    // check for a specific generic type (like Product<Integer>) 
    // at runtime. Instead, you can check against the raw type 
    // or use a wildcard.
  }

  public void read() {
    System.out.print("Which product would you like to  to read: ");
    Integer id = sc.nextInt();
    System.out.println(service.get(id));
  }

  public void delete() {
    System.out.print("Which product would you like to  to delete: ");
    Integer id = sc.nextInt();
    if (service.delete(id) == 1)
      System.out.println("Product deleted");
    else
    System.out.println("Delete failed");
  }

  public void create() {
    System.out.println("Enter product: ");
    System.out.print("Product type:");
    String type = sc.next();
    System.out.print("Product name:");
    String name = sc.next();
    System.out.print("Product price:");
    Double price = sc.nextDouble();
    System.out.print("Product quantity:");
    int quantity = sc.nextInt();
    Product product = new Product(type, name, price, quantity);
    
    if (service.create(product) == 1)
      System.out.println("Product created");
    else
    System.out.println("Create failed");
  }

  public void update() {
    System.out.println("Select product: ");
    all();
    Integer id = sc.nextInt();
    Product product = service.get(id);
    sc.nextLine();

    System.out.print("Product name:");
    String name = sc.nextLine();
    if (name != "" ) {
      product.setName(name);
    }
    System.out.print("Product price:");
    String price = sc.nextLine();
    if (price != "" ) {
     product.setPrice(Double.parseDouble(price));
    }

    System.out.print("Product quantity:");
    String quantity = sc.nextLine();
    if (quantity != "" ) {
      product.setQuantity(Integer.parseInt(quantity));
     }

    if (service.update(product) == 1)
      System.out.println("Product updated");
    else
    System.out.println("Update failed");
  }
}
