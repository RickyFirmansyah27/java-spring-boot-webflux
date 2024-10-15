package com.myapp.simple.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.myapp.simple.model.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, Long>{
  Flux<Product> findAll();
  
  Mono<Product> findById(Long id);

  Mono<Product> findByName(String Name);
}
