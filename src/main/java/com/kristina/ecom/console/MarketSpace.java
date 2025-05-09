package com.kristina.ecom.console;

import java.util.Scanner;

import com.kristina.ecom.domain.ShoppingCart;
import com.kristina.ecom.domain.Status;
import com.kristina.ecom.service.ProductService;
import com.kristina.ecom.service.ShoppingCartService;
import com.kristina.ecom.domain.Component;
import com.kristina.ecom.domain.Computer;
import com.kristina.ecom.domain.ComputerBase;
import com.kristina.ecom.domain.Product;

import java.util.Map;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.bson.types.ObjectId;

public class MarketSpace {
  private static  MarketSpace instance = new MarketSpace();
  private Map<Integer, Product> products;
  private ShoppingCart shoppingCart;
  private ShoppingCartService shopService;
  private ProductService productService;
 

  private MarketSpace() {
    products = new HashMap<>();
    shopService = new ShoppingCartService();
    productService = new ProductService();
    this.shoppingCart = shopService.read("98765"); //gets only ACTIVE
    if (shoppingCart == null )
      shoppingCart = new ShoppingCart(new ObjectId().toHexString(), "98765", new Date(), Status.NEW, new ArrayList<>());
  }

  public static MarketSpace instance() {
    return instance;
  }

  public void buy() {
    new ProductService().getAll().forEach((product) -> products.put(product.getId(), product));

    // Computer computerStock = new ComputerBase(); // this reads from db, so it has quantity from stock
    // System.out.println("🍀  Check computerStock: " + computerStock.getBase().getQuantity());

    Computer computerOrder= new ComputerBase();
    computerOrder.getBase().setQuantity(1);
    System.out.println("🍀  Check computerOrder: " + computerOrder.getBase().getQuantity());


    Boolean cancel = false;
    Scanner sc = new Scanner(System.in);
    int c = 0;

    while (true) {
      System.out.printf("Current Build: %s, and total price is %.2f\n", computerOrder.getDescription(), computerOrder.getPrice());
      System.out.println("What component would you like to add?");
      menu();

      c = sc.nextInt();
      if (c == -1) {
        cancel = true;
        break;
      } 
      
      if (c == 0)
        break;

      if  (products.keySet().contains(c)) { // products.containsKey(c) // more optimal
        Product product = products.get(c);

        if (product.getQuantity() == 0) {
          System.out.println("Out of stock. Select another product.");

        } else {
          Product p = new Product();
          try {
            p = (Product) product.clone();
            p.setQuantity(1);
          } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
          } 
          
          computerOrder = new Component(computerOrder, p); // decorator wrapping the computer
          product.setQuantity(product.getQuantity() -1);
        }
      } else {
        System.out.println("Invalid choice. Please try again.");
        continue;
      }
    }

    if (!cancel) {
      // if (shoppingCart.getStatus() == Status.COMPLETED) {
      //   shoppingCart = new ShoppingCart(new ObjectId().toHexString(), "98765", new Date(), Status.NEW, new ArrayList<>());
      // }

      // computerStock.getBase().setQuantity(computerStock.getBase().getQuantity() - 1);
      // productService.update(computerStock.getBase()); // update the stock

      shoppingCart.getComputers().add(computerOrder);
      if (shoppingCart.getStatus() == Status.NEW) {
        shopService.create(shoppingCart);
      } else if (shoppingCart.getStatus() == Status.ACTIVE) {
        shopService.update(shoppingCart);
      }
      
    } else {
      System.out.println("Order is canceled!");
    }
  }

  private void menu() {
    products.forEach((k,v) -> System.out.println(k + ":" + v ));
    System.out.println(-1 + ": " + "Cancel");
    System.out.println(0 + ": " + "Done");
  }

  public ShoppingCart getCart() {
    return shoppingCart;
  }
}
