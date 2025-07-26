package com.poly.ai.management.dao.redis.repository;

import com.poly.ai.management.dao.redis.model.RedisChatSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataRedisChatSessionRepository extends CrudRepository<RedisChatSession, String> {
}
