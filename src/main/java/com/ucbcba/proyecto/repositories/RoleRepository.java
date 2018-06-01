package com.ucbcba.proyecto.repositories;

import com.ucbcba.proyecto.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import javax.transaction.Transactional;

@Transactional
public interface RoleRepository extends JpaRepository<Role, Integer> {
}