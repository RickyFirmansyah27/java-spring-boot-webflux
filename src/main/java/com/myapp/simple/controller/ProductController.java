package com.myapp.simple.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.myapp.simple.response.BaseResponse;
import com.myapp.simple.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class ProductController {
  @Autowired
  ProductService productService;

  private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

  @GetMapping("/products")
  @ResponseStatus(HttpStatus.OK)
  public Mono<BaseResponse<List<Product>>> getAllProducts(@RequestParam(required = false) String name) {
    logger.info("[ProductController.getAllProducts] Request received. Filter by name: {}", name);
    Flux<Product> product = productService.findAll();

    return product
        .collectList()
        .map(data -> {
          if (data.isEmpty()) {
            logger.info("[ProductController.getAllProducts] No products found.");
            return new BaseResponse<>("success", "No products found", data);
          } else {
            logger.info("[ProductController.getAllProducts] Products fetched successfully: {}", data);
            return new BaseResponse<>("success", "Products fetched successfully", data);
          }
        })
        .onErrorResume(e -> {
          logger.error("[ProductController.getAllProducts] Error occurred: {}", e.getMessage());
          return Mono.just(
              new BaseResponse<>("error", "Failed to fetch products: " + e.getMessage(), Collections.emptyList()));
        });
  }

  @PostMapping("/products")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<BaseResponse<Product>> createProduct(@RequestBody Product product) {
    logger.info("[ProductController.createProduct] Request received to create product: {}", product);
    return productService.save(product)
        .map(savedProduct -> {
          logger.info("[ProductController.createProduct] Product created successfully: {}", savedProduct);
          return new BaseResponse<>("success", "Product created successfully", savedProduct);
        })
        .onErrorResume(e -> {
          logger.error("[ProductController.createProduct] Error occurred: {}", e.getMessage());
          return Mono.just(new BaseResponse<>("error", "Failed to create product: " + e.getMessage(), null));
        });
  }

  @PutMapping("/products/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<BaseResponse<Product>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
    logger.info("[ProductController.updateProduct] Request received to update product with ID: {}. Data: {}", id, product);
    return productService.update(id, product)
        .map(updatedProduct -> {
          logger.info("[ProductController.updateProduct] Product updated successfully: {}", updatedProduct);
          return new BaseResponse<>("success", "Product updated successfully", updatedProduct);
        })
        .onErrorResume(e -> {
          logger.error("[ProductController.updateProduct] Error occurred: {}", e.getMessage());
          return Mono.just(new BaseResponse<>("error", "Failed to update product: " + e.getMessage(), null));
        });
  }

  @GetMapping("/products/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<BaseResponse<Product>> getProductById(@PathVariable Long id) {
    logger.info("[ProductController.getProductById] Request received to fetch product by ID: {}", id);
    return productService.findById(id)
        .map(product -> {
          logger.info("[ProductController.getProductById] Product fetched successfully: {}", product);
          return new BaseResponse<>("success", "Product fetched successfully", product);
        })
        .switchIfEmpty(Mono.just(new BaseResponse<>("success", "Product not found", null)))
        .onErrorResume(e -> {
          logger.error("[ProductController.getProductById] Error occurred: {}", e.getMessage());
          return Mono.just(new BaseResponse<>("error", "Failed to fetch product: " + e.getMessage(), null));
        });
  }

  @DeleteMapping("/products/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<BaseResponse<Object>> deleteProduct(@PathVariable Long id) {
    logger.info("[ProductController.deleteProduct] Request received to delete product with ID: {}", id);
    return productService.deleteById(id)
        .then(Mono.just(new BaseResponse<>("success", "Product deleted successfully", null)))
        .onErrorResume(e -> {
          logger.error("[ProductController.deleteProduct] Error occurred: {}", e.getMessage());
          return Mono.just(new BaseResponse<>("error", "Failed to delete product: " + e.getMessage(), null));
        });
  }

  @DeleteMapping("/products")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<BaseResponse<Object>> deleteAllProducts() {
    logger.info("[ProductController.deleteAllProducts] Request received to delete all products.");
    return productService.deleteAll()
        .then(Mono.just(new BaseResponse<>("success", "All products deleted successfully", null)))
        .onErrorResume(e -> {
          logger.error("[ProductController.deleteAllProducts] Error occurred: {}", e.getMessage());
          return Mono.just(new BaseResponse<>("error", "Failed to delete products: " + e.getMessage(), null));
        });
  }
}
