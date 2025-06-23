package com.poly.authentication.service.dao.user.repository.custom;

import jakarta.persistence.EntityManager;

public interface CustomUserRepository {

    EntityManager getEntityManager();
}
