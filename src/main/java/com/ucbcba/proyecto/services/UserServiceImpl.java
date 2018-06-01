package com.ucbcba.proyecto.services;

import com.ucbcba.proyecto.entities.Role;
import com.ucbcba.proyecto.entities.Users;
import com.ucbcba.proyecto.repositories.RoleRepository;
import com.ucbcba.proyecto.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(Users user) {
        Role userRole = roleRepository.findOne(1);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    @Override
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Users findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserEdited(Users user){
        userRepository.save(user);
    }

    @Override
    public List<Users> listAll() {
        return userRepository.findAll();
    }
}