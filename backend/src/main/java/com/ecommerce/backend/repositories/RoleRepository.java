package com.ecommerce.backend.repositories;

import com.ecommerce.backend.model.AppRole;
import com.ecommerce.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
