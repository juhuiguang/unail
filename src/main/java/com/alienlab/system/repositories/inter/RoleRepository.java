package com.alienlab.system.repositories.inter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alienlab.system.repositories.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

}
