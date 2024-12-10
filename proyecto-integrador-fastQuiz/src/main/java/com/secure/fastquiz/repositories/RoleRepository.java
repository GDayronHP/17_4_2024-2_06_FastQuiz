package com.secure.fastquiz.repositories;

import com.secure.fastquiz.models.AppRole;
import com.secure.fastquiz.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);

}