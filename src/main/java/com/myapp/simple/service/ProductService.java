package com.myapp.simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.simple.model.Product;
import com.myapp.simple.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

  @Autowired
  ProductRepository productRepository;

  public Flux<Product> findAll() {
    return productRepository.findAll();
  }

  public Mono<Product> findById(Long id) {
    return productRepository.findById(id);
  }

  public Mono<Product> findProductByName(String name) {
    return productRepository.findByName(name);
  }

  public Mono<Product> save(Product Product) {
    return productRepository.save(Product);
  }

  public Mono<Product> update(Long id, Product product) {
    return productRepository.findById(id)
        .flatMap(existingProduct -> {
            // Update field yang diperlukan
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            
            // Simpan perubahan
            return productRepository.save(existingProduct);
        });
}

  public Mono<Void> deleteById(Long id) {
    return productRepository.deleteById(id);
  }

  public Mono<Void> deleteAll() {
    return productRepository.deleteAll();
  }

}
