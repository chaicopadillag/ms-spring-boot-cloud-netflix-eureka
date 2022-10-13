package com.dev.team.item.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dev.team.item.models.Item;
import com.dev.team.item.models.service.ItemService;

import dev.team.commons.models.entity.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RefreshScope
@RestController
public class ItemController {

    @Autowired
    private Environment env;

    @Autowired
    private CircuitBreakerFactory cbFactory;

    @Autowired
    @Qualifier("serviceFeign")
    private ItemService itemService;

    @Value("${configuration.text}")
    private String text;

    @GetMapping("/items")
    public List<Item> items() {
        return itemService.findAll();
    }

    @GetMapping("/item/{id}/{quantity}")
    public Item itemDetail(@PathVariable Long id, @PathVariable Integer quantity) {
        return cbFactory.create("items").run(() -> itemService.findByIdAndQuantity(id, quantity));
    }

    @CircuitBreaker(name = "items")
    @TimeLimiter(name = "items")
    @GetMapping("/item-detail/{id}/{quantity}")
    public Item itemDetailTwo(@PathVariable Long id, @PathVariable Integer quantity) {
        return itemService.findByIdAndQuantity(id, quantity);
    }

    @GetMapping("/view-config")
    public ResponseEntity<?> getConfig(@Value("${server.port}") String port) {
        Map<String, String> json = new HashMap<>();
        System.out.println(text);
        json.put("port", port);
        json.put("text", text);

        if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
            json.put("autor.name", env.getProperty("configuration.autor.name"));
            json.put("autor.email", env.getProperty("configuration.autor.email"));
        }

        return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Product crear(@RequestBody Product product) {
        return itemService.save(product);
    }

    @PutMapping("/editar/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Product editar(@RequestBody Product product, @PathVariable Long id) {
        return itemService.update(product, id);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }

}
