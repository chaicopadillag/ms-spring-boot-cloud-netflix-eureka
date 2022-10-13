package com.dev.team.product.models.service;

import java.util.List;

import dev.team.commons.models.entity.Product;

public interface IProductService {

    public List<Product> findAll();

    public Product finById(Long id);

    public Product save(Product product);

    public void deleteById(Long id);
}
