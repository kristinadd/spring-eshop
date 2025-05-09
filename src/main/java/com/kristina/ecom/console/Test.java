package com.kristina.ecom.console;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.kristina.ecom.domain.Product;

public class Test {

  public static void main(String[] args) {

    // Can do the same thing with Set
    // Product p1 =  new Product("1", "Printer", 10, 10);
    //
    Product p2 =  new Product("2", "GPUs", 20, 10);
    Product p3 =  new Product("3", "USB", 30, 10);
    //
    Product p4 =  new Product("2", "GPUs", 20, 10);
    Product p5 =  new Product("3", "USB", 30, 10);
    //
    Product p6 =  new Product("4", "Microphone", 40, 10);


    List<Product> l1 = Arrays.asList(p2, p3);
    List<Product> l2 = Arrays.asList(p4, p5);

    // The same as doing 
    // List<Product> l1 = new ArrayList<>();
    // l1.add(p2);
    // l1.add(p3);

    // Fixed-Size List:
    // The List returned by Arrays.asList() is fixed in size:
    // You can modify elements in the list, but you cannot add or remove elements.

    // List<String> list = Arrays.asList("A", "B", "C");
    // list.set(0, "X"); // Allowed: Modifies the first element
    // list.add("D");    // Throws UnsupportedOperationException

    // you can use new ArrayList<>(Arrays.asList(...)) to create a dynamic list


    // the common one // update
    List<Product> commanList = l1.stream().filter(product -> l2.contains(product)).collect(Collectors.toList());
    System.out.println(commanList);

    //in the first only // add 
    List<Product> onlyFisrt = l1.stream().filter(s -> !l2.contains(s)).collect(Collectors.toList());
    System.out.println(onlyFisrt);

    // is in the second list only // delete
    List<Product> onlySecond = l2.stream().filter(s -> !l1.contains(s)).collect(Collectors.toList());
    System.out.println(onlySecond);

    // Using SET
    Set<Product> set1 = new HashSet<>(Arrays.asList(p2, p3));
    Set<Product> set2 = new HashSet<>(Arrays.asList(p4, p5, p6));

    // The :: syntax in Java is called the method reference operator. It allows you to refer to methods or constructors 
    // directly by their name, rather than invoking them. It is a shorthand for a lambda expression that calls a specific method.
    // The set2::contains is a method reference to the contains() method of the Set interface, applied to the set2 object.
    Set<Product> commonProducts = set1.stream()
    .filter(set2::contains) // or .filter(product -> set2.contains(product)) it's the same thing
    .collect(Collectors.toSet());
    System.out.println("Common Products: " + commonProducts);

    Set<Product> onlyInFirst = set1.stream()
    .filter(product -> !set2.contains(product))
    .collect(Collectors.toSet());
    System.out.println("Products only in the first set: " + onlyInFirst);

    Set<Product> onlyInSecond = set2.stream()
    .filter(product -> !set1.contains(product))
    .collect(Collectors.toSet());
    System.out.println("Products only in the second set: " + onlyInSecond);
  }
}
