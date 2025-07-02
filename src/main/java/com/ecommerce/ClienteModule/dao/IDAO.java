package com.ecommerce.ClienteModule.dao;

import java.util.List;

public interface IDAO<Entity> {
    void save(Entity entity);
    void update(Entity entity);
    Entity findById(Long id);
    List<Entity> findAll();
    void delete(Long id);
}
