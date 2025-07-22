package com.poly.authentication.service.dao.token.repository;

import com.poly.authentication.service.dao.token.entity.ForgotPasswordTokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgotPasswordTokenRedisRepository extends CrudRepository<ForgotPasswordTokenEntity, String> {

}
