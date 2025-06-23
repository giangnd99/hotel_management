package com.springboot.asm.fpoly_asm_springboot.repository.custom;

import jakarta.persistence.EntityManager;

public interface CustomUserRepository {

    EntityManager getEntityManager();
}
