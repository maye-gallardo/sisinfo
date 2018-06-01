package com.ucbcba.proyecto.repositories;

import com.ucbcba.proyecto.entities.History;
import org.springframework.data.repository.CrudRepository;
import javax.transaction.Transactional;


@Transactional
public interface HistoryRepository extends CrudRepository<History, Integer> {
}
