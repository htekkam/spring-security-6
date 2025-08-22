package com.techie.spring.security.controller;

import com.techie.spring.security.record.Product;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    List<Product> productList = new ArrayList<>(
            List.of(
                    new Product(1,"iphone",15000.0),
                    new Product(2,"samsung",10000.0)
    ));

    @GetMapping
    public List<Product> getProducts(){
        return productList;
    }

    @PostMapping
    public Product saveProduct(@RequestBody Product product){
        productList.add(product);
        return product;
    }
}
