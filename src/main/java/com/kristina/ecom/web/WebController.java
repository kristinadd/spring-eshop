package com.kristina.ecom.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.kristina.ecom.domain.Product;

import org.springframework.ui.Model;

@Controller
public class WebController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/hello2")
    public String hello2() {
        return "hello2";
    }

    @GetMapping("/product")
    public String getProduct(Model model) {
        Product product = new Product("computer", "Base computer", 999.99, "img/computer.jpg");
        model.addAttribute("product", product);
        return "product";
    }
}
