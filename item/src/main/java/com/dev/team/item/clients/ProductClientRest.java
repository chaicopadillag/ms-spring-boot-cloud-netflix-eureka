package com.dev.team.item.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dev.team.commons.models.entity.Product;

@FeignClient(name = "ms-products")
public interface ProductClientRest {

    @GetMapping("/api/products")
    public List<Product> findAll();

    @GetMapping("/api/products/{id}")
    public Product findByIdAndQuantity(@PathVariable Long id);

    @PostMapping("/api/products/crear")
    public Product crear(@RequestBody Product product);

    @PutMapping("/api/products/editar/{id}")
    public Product editar(@RequestBody Product product, @PathVariable Long id);

    @DeleteMapping("/api/products/delete/{id}")
    public void delete(@PathVariable Long id);

}
