package com.dev.team.item.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dev.team.item.models.Item;

import dev.team.commons.models.entity.Product;

@Service("serviceRestTemplate")
public class ItemServiceImp implements ItemService {

    @Autowired
    private RestTemplate clientRest;

    @Override
    public List<Item> findAll() {

        List<Product> products = Arrays
                .asList(clientRest.getForObject("http://localhost:8082/api/products", Product[].class));

        return products.stream().map(product -> new Item(product, 1)).collect(Collectors.toList());

    }

    @Override
    public Item findByIdAndQuantity(Long id, Integer quantity) {

        Map<String, String> pathVars = new HashMap<String, String>();

        pathVars.put("id", id.toString());

        Product product = clientRest.getForObject("http://localhost:8082/api/products/{id}", Product.class, pathVars);

        return new Item(product, quantity);
    }

    @Override
    public Product save(Product product) {
        HttpEntity<Product> body = new HttpEntity<Product>(product);

        ResponseEntity<Product> res = clientRest.exchange("http://localhost:8082/api/products/create", HttpMethod.POST,
                body, Product.class);

        Product prodRes = res.getBody();
        return prodRes;
    }

    @Override
    public Product update(Product product, Long id) {

        Map<String, String> pathVars = new HashMap<String, String>();

        pathVars.put("id", id.toString());

        HttpEntity<Product> body = new HttpEntity<Product>(product);

        ResponseEntity<Product> res = clientRest.exchange("http://localhost:8082/api/products/edit/{id}",
                HttpMethod.PUT,
                body, Product.class, pathVars);

        return res.getBody();
    }

    @Override
    public void delete(Long id) {

        Map<String, String> pathVars = new HashMap<String, String>();

        pathVars.put("id", id.toString());
        clientRest.delete("http://localhost:8082/api/products/edit/{id}", pathVars);

    }

}
