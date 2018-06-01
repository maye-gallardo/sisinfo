package com.ucbcba.proyecto.repositories;

import com.ucbcba.proyecto.entities.Category;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface CategoryRepository extends CrudRepository<Category, Integer>{

}
