package com.poly.authentication.service.dao.user.adapter;

import com.poly.authentication.service.dao.user.mapper.UserJpaMapper;
import com.poly.authentication.service.dao.user.repository.UserJpaRepository;
import com.poly.authentication.service.domain.entity.User;
import com.poly.authentication.service.domain.port.out.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaMapper userJpaMapper;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByEmail(String email) {

        return userJpaRepository.findByEmail(email).map(userJpaMapper::toDomainEntity);
    }

    @Override
    public User save(User user) {
        return userJpaMapper.toDomainEntity(userJpaRepository.save(userJpaMapper.toEntity(user)));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userJpaRepository.findAll(pageable).map(userJpaMapper::toDomainEntity);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return userJpaRepository.findById(userId).map(userJpaMapper::toDomainEntity);
    }

    @Override
    public void deleteById(UUID userId) {
        userJpaRepository.deleteById(userId);
    }
}
