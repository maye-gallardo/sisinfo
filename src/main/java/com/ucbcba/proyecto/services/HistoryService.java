package com.ucbcba.proyecto.services;

import com.ucbcba.proyecto.entities.History;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import java.util.List;

public interface HistoryService {

    Iterable<History> listAllHistories();

    void saveHistory(History post);

    History getHistory(Integer id);

    void deleteHistory(Integer id);






}
