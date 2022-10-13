package com.dev.team.item.models.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.team.item.clients.ProductClientRest;
import com.dev.team.item.models.Item;

import dev.team.commons.models.entity.Product;

@Service("serviceFeign")
public class ItemServiceFeign implements ItemService {

    @Autowired
    private ProductClientRest clientFeign;

    @Override
    public List<Item> findAll() {
        return clientFeign.findAll().stream().map(product -> new Item(product,
                1)).collect(Collectors.toList());
    }

    @Override
    public Item findByIdAndQuantity(Long id, Integer quantity) {
        return new Item(clientFeign.findByIdAndQuantity(id), quantity);
    }

    @Override
    public Product save(Product product) {

        return clientFeign.crear(product);
    }

    @Override
    public Product update(Product product, Long id) {

        return clientFeign.editar(product, id);
    }

    @Override
    public void delete(Long id) {

        clientFeign.delete(id);

    }

}
