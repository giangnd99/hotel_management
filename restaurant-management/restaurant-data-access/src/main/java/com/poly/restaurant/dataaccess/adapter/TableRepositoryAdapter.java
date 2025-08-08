package com.poly.restaurant.dataaccess.adapter;

import com.poly.restaurant.application.port.out.TableRepositoryPort;
import com.poly.restaurant.dataaccess.entity.TableJpaEntity;
import com.poly.restaurant.dataaccess.jpa.JpaTableRepository;
import com.poly.restaurant.dataaccess.mapper.TableEntityMapper;
import com.poly.restaurant.domain.entity.Table;
import com.poly.restaurant.domain.entity.TableStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TableRepositoryAdapter implements TableRepositoryPort {

    private final JpaTableRepository jpaTableRepository;

    @Override
    public Table save(Table table) {
        TableJpaEntity entity = TableEntityMapper.toEntity(table);
        return TableEntityMapper.toDomain(jpaTableRepository.save(entity));
    }

    @Override
    public Optional<Table> findById(String id) {
        return jpaTableRepository.findById(id)
                .map(TableEntityMapper::toDomain);
    }

    @Override
    public List<Table> findAll() {
        return jpaTableRepository.findAll().stream()
                .map(TableEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(String id) {
        return jpaTableRepository.existsById(id);
    }

    @Override
    public void deleteById(String id) {
        jpaTableRepository.deleteById(id);
    }

    @Override
    public Optional<Table> findByNumber(int number) {
        return jpaTableRepository.findByNumber(number)
                .map(TableEntityMapper::toDomain);
    }

    @Override
    public List<Table> findByStatus(TableStatus status) {
        return jpaTableRepository.findByStatus(status).stream()
                .map(TableEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNumber(int number) {
        return jpaTableRepository.existsByNumber(number);
    }

    @Override
    public List<Table> findAvailableTables() {
        return jpaTableRepository.findByStatus(TableStatus.AVAILABLE).stream()
                .map(TableEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}