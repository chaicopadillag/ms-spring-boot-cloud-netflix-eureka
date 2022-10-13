package com.dev.team.item.models.service;

import java.util.List;

import com.dev.team.item.models.Item;

import dev.team.commons.models.entity.Product;

public interface ItemService {

    public List<Item> findAll();

    public Item findByIdAndQuantity(Long id, Integer quantity);

    public Product save(Product product);

    public Product update(Product product, Long id);

    public void delete(Long id);

}
