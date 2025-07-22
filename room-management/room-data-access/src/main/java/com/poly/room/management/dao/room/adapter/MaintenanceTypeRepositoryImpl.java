package com.poly.room.management.dao.room.adapter;

import com.poly.room.management.dao.room.mapper.MaintenanceTypeMapper;
import com.poly.room.management.dao.room.repository.MaintenanceTypeJpaRepository;
import com.poly.room.management.domain.entity.MaintenanceType;
import com.poly.room.management.domain.port.out.repository.MaintenanceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MaintenanceTypeRepositoryImpl implements MaintenanceTypeRepository {
    private final MaintenanceTypeJpaRepository jpaRepository;
    private final MaintenanceTypeMapper mapper;

    @Override
    public MaintenanceType save(MaintenanceType maintenanceType) {
        return mapper.toDomain(
                jpaRepository.save(mapper.toEntity(maintenanceType))
        );
    }

    @Override
    public Optional<MaintenanceType> findById(Integer id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public MaintenanceType update(MaintenanceType maintenanceType) {
        return mapper.toDomain(
                jpaRepository.save(mapper.toEntity(maintenanceType))
        );
    }

    @Override
    public void delete(MaintenanceType maintenanceType) {
        jpaRepository.delete(mapper.toEntity(maintenanceType));
    }

    @Override
    public List<MaintenanceType> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}