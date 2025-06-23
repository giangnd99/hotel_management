package com.springboot.asm.fpoly_asm_springboot.repository.primary;

import com.springboot.asm.fpoly_asm_springboot.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
