package com.ucbcba.proyecto.services;

import com.ucbcba.proyecto.entities.Users;

import java.util.List;

public interface UserService {
    void save(Users user);
    Users findUserByEmail(String email);
    void saveUserEdited(Users user);
    Users findByUsername(String username);
    List<Users> listAll();

}