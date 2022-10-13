package com.dev.team.product.models.repository;

import org.springframework.data.repository.CrudRepository;

import dev.team.commons.models.entity.Product;

public interface ProductoRepository extends CrudRepository<Product, Long> {

}
