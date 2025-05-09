package com.kristina.ecom.console;

import java.util.Scanner;
public class Main {

  private static MarketSpace marketSpace = MarketSpace.instance();
  private static Admin admin;
  private static Oms oms;
  private Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        CartManager cartManager = new CartManager(marketSpace.getCart());
        admin = Admin.instance();
        oms = Oms.instance();
        Main m = new Main();
        int c;
        

        while (true) {
          m.menu();
          c = m.sc.nextInt();
    
          switch (c) {
            case 1:
              marketSpace.buy();
              break;
            case 2:
              cartManager.admin();
              break;
            case 3:
              admin.admin();
              break;
            case 4:
              oms.admin();
              break;
            case 5:
              System.exit(0);
            default:
              System.out.println("Invalid choice. Please try again.");
          }
        }
    }

    private void menu() {
    String[] items = {
      "Buy a computer",
      "Shopping cart",
      "Product Admin",
      "Order managment",
      "Quit"
    };

    System.out.println("Hi, what would you like to do?");
      for (int i = 0; i < items.length; i++)
        System.out.printf("%d: %s\n", i+1, items[i]);
    }
}
