package com.dev.team.product.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dev.team.product.models.service.ProductService;

import dev.team.commons.models.entity.Product;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/products/{id}")
    public Product productInfo(@PathVariable Long id) {
        return productService.finById(id);
    }

    @PostMapping("/products/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        return productService.save(product);
    }

    @PutMapping("/products/edit/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Product editar(@RequestBody Product product, @PathVariable Long id) {
        Product dbProd = productService.finById(id);

        dbProd.setName(product.getName());
        dbProd.setPrice(product.getPrice());
        return productService.save(dbProd);

    }

    @DeleteMapping("/products/delete/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        productService.deleteById(id);
    }

}
