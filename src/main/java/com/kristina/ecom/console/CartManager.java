package com.kristina.ecom.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.kristina.ecom.dao.DAOException;
import com.kristina.ecom.domain.Computer;
import com.kristina.ecom.domain.ComputerBase;
import com.kristina.ecom.domain.Order;
import com.kristina.ecom.domain.Product;
import com.kristina.ecom.domain.ShoppingCart;
import com.kristina.ecom.domain.SortByOrderID;
import com.kristina.ecom.domain.SortByPrice;
import com.kristina.ecom.domain.SortStrategy;
import com.kristina.ecom.domain.Status;
import com.kristina.ecom.service.OrderService;
import com.kristina.ecom.service.ProductService;
import com.kristina.ecom.service.ShoppingCartService;

public class CartManager {
  private Scanner sc;
  private ShoppingCart shoppingCart;
  private SortStrategy strategy, sortByOrderIDStrategy, sortByPriceStrategy;
  private ShoppingCartService shopService = new ShoppingCartService();
  private Computer computer;
  private Computer computerStock;
  private ProductService productService;



  public CartManager(ShoppingCart shoppingCart) {
    sc = new Scanner(System.in);
    this.shoppingCart = shoppingCart;
    sortByOrderIDStrategy = new SortByOrderID();
    sortByPriceStrategy = new SortByPrice();
    computerStock = new ComputerBase();
    productService = new ProductService();
  }

  public void admin() {
    while (true) {
      menu();
      int c = sc.nextInt();
      sc.nextLine();  // Consume the leftover newline
      switch (c) {
        case 1:
          getCarts();
          break;
        case 2:
          checkOut();
          break;
        case 3:
          return;
        case 4:
          System.out.println("Provide a shopping cart id: ");
          String input = sc.nextLine();
          getCart(input);
          break;
        case 5:
          update();
          break;
        case 6:
          System.out.println("Provide a shopping cart id: ");
          input = sc.nextLine();
          delete(input);
          break;
        case 7:
          cancel();
          break;
        default:
          System.out.println("❌ Invalid choice. Please try again.");
      }
    }
  }

  private void menu() {
    String[] items = {
      "See all shopping carts",
      "Check out",
      "Return to main menu",
      "See specific shopping cart",
      "Edit shopping cart",
      "Delete shopping cart",
      "Cancel shopping cart"
    };

    System.out.println("Shopping Cart details: ");

    for (int i = 0; i< items.length; i++) {
      System.out.printf("%d: %s\n", i+1, items[i]);
    }
  }

    private void shoppingCartUpdateMenu() {
    String[] shoppingCartUpdateMenu = {
      "1: Select Base Computer",
      "2: Delete a product from the cart",
      "3: Add product to the cart",
      "4: Remove product from the cart",
      "5: Update product quantity in the cart",
      "6: Done"
    };

    System.out.println("\n*** Shopping Cart Update Menu ***");
    System.out.println(shoppingCart);
    Arrays.stream(shoppingCartUpdateMenu).forEach(System.out::println);
  }

  public void sort(String key) {
    if (shoppingCart.getComputers().isEmpty()) {
      System.out.println("No items");
      return;
    }
    if (key.equals("ID"))
      this.strategy = this.sortByOrderIDStrategy;
    else if (key.equals("PRICE"))
      this.strategy = this.sortByPriceStrategy;

    this.strategy.sort(shoppingCart.getComputers());
  }

  public void getCarts() {
    List<ShoppingCart> carts = new ArrayList<>();
    carts = shopService.readAll();

    if (carts.isEmpty())
      System.out.println("❌ No available shopping carts");
    else
    for (ShoppingCart cart : carts) {
      System.out.println(cart);
    }
  }

  public ShoppingCart getCart(String id) {
    ShoppingCart cart;
    cart = shopService.readId(id);

    if (cart == null) {
      System.out.println("❌ Coudn't find the shopping cart with id: " + id);
      return null;
    } else {
      System.out.println(cart);
      return cart;
    }
  }

  public void checkOut() {
    if (shoppingCart.getStatus() == Status.CANCELED || shoppingCart.getComputers().isEmpty() == true) {
      System.out.println("❌ Can't check out. Cart is either empty or canceled");
    }

    boolean outOfStock = false;
    OrderService service = new OrderService();

    List<Computer> computers = shoppingCart.getComputers();
    for (Computer computer : computers) {
      Order order = new Order(computer);
      try {
      service.create(order);
      } catch (DAOException ex) {
        System.out.println("❌ " + ex.getMessage());
        outOfStock = true;
      } 
    }

    if (!outOfStock) {

      computerStock.getBase().setQuantity(computerStock.getBase().getQuantity() - 1);
      productService.update(computerStock.getBase()); // update the stock

      shoppingCart.setStatus(Status.COMPLETED);
      shopService.update(shoppingCart);
      shoppingCart.getComputers().clear();
      shoppingCart.setStatus(Status.NEW);
    }
  }
  
  public void update() {
    System.out.println("*** Update Shopping Cart ***");
 
    boolean isDirty = false;
    boolean updating = true;
    while (updating) {
      shoppingCartUpdateMenu();
      int c = sc.nextInt();
      switch (c) {
        case 1:
          selectComputer();
          break;
        case 2:
          if (computer == null)
            selectComputer();
          deleteProductFromCart();
          isDirty = true;
          break;
        case 3:
          if (computer == null)
            selectComputer();
          addProductToCart();
          isDirty = true;
          break;
        case 4:
          if (computer == null)
            selectComputer();
          removeProductFromCart();
          isDirty = true;
          break;
        case 5:
          if (computer == null)
            selectComputer();
          updateProducts();
          isDirty = true;
          break;
        case 6:
          updating = false;
          break;
          // return; it works but thats not the correct way to write it
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }

    if (isDirty) {
      shopService.update(shoppingCart);
    } else {
      System.out.println("No change");
    } 
  }

  public int delete(String id) {
    shoppingCart.getComputers().clear();
    shoppingCart.setStatus(Status.NEW);
    return shopService.delete(id);
  }

  public void cancel() {
    shopService.cancel(shoppingCart);
  }


  public void selectComputer() {
    System.out.println(shoppingCart);
    System.out.println("Choose a Base Computer to be updated");
    int c = sc.nextInt();
    computer = shoppingCart.getComputers().get(c -1);
  }

  public void deleteProductFromCart() {
    List<Product> products = computer.getComponents();
    for (int i=0; i<products.size(); i++) {
      System.out.println(i+1 + " :" + products.get(i) );
    }

    System.out.println("Choose a Product to be deleted");
    int c = sc.nextInt();
    products.remove(c -1);
  }

  public void addProductToCart() {
    System.out.println("🍀 Select product to add: ");
    ProductService productService = new ProductService();
    List<Product> products = productService.getAll();
    int productIndex = selectProduct(products);
    int productId = products.get(productIndex).getId();
    Product productFromStock = productService.get(productId);
    Product productInCart = null;

    System.out.println("Type Prouct Quantity: ");
    int c = sc.nextInt();

    try {
      productInCart = (Product) productFromStock.clone();
      productInCart.setQuantity(c);
    } catch (CloneNotSupportedException ex) {
        ex.printStackTrace();
      };
    
    if (productInCart != null) {
      computer.getComponents().add(productInCart);
      System.out.println("✅ Product added successfully!");
    } else {
      System.out.println("❌ Product was not added!");
    }
  }

  public void removeProductFromCart() {
    List<Product> products = computer.getComponents();
    int productId = selectProduct(products);
    
    if (products.isEmpty() == false) {
      products.remove(productId - 1);
      System.out.println("✅ Product removed successfully!");
    } else {
      System.out.println("❌ Product was not removed!");
    }
  }

  private int selectProduct(List <Product> products) {
    int productIndex;
    boolean invalid;

    do {
      System.out.println("Select the product:");
      for (int i = 0; i < products.size(); i++) {
          System.out.println((i + 1) + ": " + products.get(i));
      }
    productIndex = sc.nextInt() - 1;
    invalid = productIndex < 0 || productIndex >= products.size();
    if (invalid)
      System.out.println("Invalid product, please choose again");
    } while (invalid);


    return productIndex;
  }

  private void updateProducts() {
    int productIndex = selectProduct(computer.getComponents());

    System.out.println("Enter the new quantity ( 0 to remove ):");
    // TbC: handle out of stock situation
    int newQuantity =  sc.nextInt();

    if (newQuantity == 0) 
      computer.getComponents().remove(productIndex);
    else
      computer.getComponents().get(productIndex).setQuantity(newQuantity);

  }
}
