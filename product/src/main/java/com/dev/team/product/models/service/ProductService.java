package com.dev.team.product.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.team.product.models.repository.ProductoRepository;

import dev.team.commons.models.entity.Product;

@Service

public class ProductService implements IProductService {

    @Autowired
    private ProductoRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {

        return (List<Product>) productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Product finById(Long id) {

        return productRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Product save(Product product) {

        return productRepository.save(product);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

}
