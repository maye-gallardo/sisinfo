package com.ucbcba.proyecto.services;

import com.ucbcba.proyecto.entities.Category;

public interface CategoryService {
    Iterable<Category> listAllCategorys();

    void saveCategory(Category category);

    Category getCategory(Integer id);

    void deleteCategory(Integer id);
}
