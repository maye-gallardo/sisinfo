package com.ucbcba.proyecto.services;

import com.ucbcba.proyecto.entities.History;
import com.ucbcba.proyecto.repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService{

    private HistoryRepository historyRepository;

    @Autowired
    @Qualifier(value = "historyRepository")
    public void setHistoryRepository(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public Iterable<History> listAllHistories() {
        return historyRepository.findAll();
    }

    @Override
    public void saveHistory(History history) {
        historyRepository.save(history);
    }

    @Override
    public History getHistory(Integer id) {
        return historyRepository.findOne(id);
    }

    @Override
    public void deleteHistory(Integer id) {
        historyRepository.delete(id);
    }

}
