package com.poly.room.management.dao.room.adapter;

import com.poly.room.management.dao.room.mapper.FurnitureMapper;
import com.poly.room.management.dao.room.repository.FurnitureJpaRepository;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.port.out.repository.FurnitureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FurnitureRepositoryImpl implements FurnitureRepository {
    private final FurnitureJpaRepository jpaRepository;
    private final FurnitureMapper mapper;

    @Override
    public Optional<Furniture> findById(Integer id) {
        return Optional.ofNullable(mapper.toDomainEntity(
                jpaRepository.findById(id)
                        .orElseThrow(() -> new RoomDomainException("Furniture with id " + id + " not found"))));
    }

    @Override
    public Furniture save(Furniture furniture) {
        return mapper.toDomainEntity(
                jpaRepository.save(mapper.toEntity(furniture))
        );
    }

    @Override
    public List<Furniture> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Furniture update(Furniture furniture) {
        return mapper.toDomainEntity(
                jpaRepository.save(mapper.toEntity(furniture))
        );
    }
}