package com.myapp.simple.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.simple.model.Product;
import com.myapp.simple.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class ProductController {
  @Autowired
  ProductService productService;
  
  @GetMapping("/products")
  @ResponseStatus(HttpStatus.OK)
  public Flux<Product> getAllProducts(@RequestParam(required = false) String Name) {
    return productService.findAll();
  }

  @PostMapping("/products")
  public Mono<Product> createProduct(@RequestBody Product product) {
      return productService.save(product);
  }

  @PutMapping("/products/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
    return productService.update(id, product);
  }

  @GetMapping("/products/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Product> getProductById(@PathVariable Long id) {
    return productService.findById(id);
  }

  @DeleteMapping("/products/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteProduct(@PathVariable Long id) {
    return productService.deleteById(id);
  }

  @DeleteMapping("/products")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteAllProducts() {
    return productService.deleteAll();
  }
}
