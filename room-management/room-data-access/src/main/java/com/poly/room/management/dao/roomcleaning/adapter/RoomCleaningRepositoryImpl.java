package com.poly.room.management.dao.roomcleaning.adapter;

import com.poly.room.management.dao.roomcleaning.entity.RoomCleaningEntity;
import com.poly.room.management.dao.roomcleaning.mapper.RoomCleaningMapper;
import com.poly.room.management.dao.roomcleaning.repository.RoomCleaningJpaRepository;
import com.poly.room.management.domain.entity.RoomCleaning;
import com.poly.room.management.domain.port.out.repository.RoomCleaningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoomCleaningRepositoryImpl implements RoomCleaningRepository {

    private final RoomCleaningJpaRepository jpaRepository;
    private final RoomCleaningMapper mapper;

    @Override
    public RoomCleaning save(RoomCleaning roomCleaning) {
        RoomCleaningEntity entity = mapper.toEntity(roomCleaning);
        RoomCleaningEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<RoomCleaning> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<RoomCleaning> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public RoomCleaning update(RoomCleaning roomCleaning) {
        RoomCleaningEntity entity = mapper.toEntity(roomCleaning);
        RoomCleaningEntity updatedEntity = jpaRepository.save(entity);
        return mapper.toDomain(updatedEntity);
    }

    @Override
    public List<RoomCleaning> findByRoomId(UUID roomId) {
        return jpaRepository.findByRoomId(roomId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findByRoomIdAndStatus(UUID roomId, String status) {
        return jpaRepository.findByRoomIdAndStatus(roomId, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findByStatus(String status) {
        RoomCleaningEntity.CleaningStatus cleaningStatus = RoomCleaningEntity.CleaningStatus.valueOf(status);
        return jpaRepository.findByStatus(cleaningStatus).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findByStatusWithPagination(String status, int page, int size) {
        return jpaRepository.findByStatusWithPagination(status, PageRequest.of(page, size))
                .getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findByScheduledDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByScheduledDateBetween(startDate, endDate).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findByCompletedDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByCompletedDateBetween(startDate, endDate).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findByRequestedDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByRequestedDateBetween(startDate, endDate).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findByCleaningType(String cleaningType) {
        RoomCleaningEntity.CleaningType type = RoomCleaningEntity.CleaningType.valueOf(cleaningType);
        return jpaRepository.findByCleaningType(type).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findByCleaningTypeAndStatus(String cleaningType, String status) {
        return jpaRepository.findByCleaningTypeAndStatus(cleaningType, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findByPriority(String priority) {
        RoomCleaningEntity.CleaningPriority cleaningPriority = RoomCleaningEntity.CleaningPriority.valueOf(priority);
        return jpaRepository.findByPriority(cleaningPriority).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findByPriorityAndStatus(String priority, String status) {
        return jpaRepository.findByPriorityAndStatus(priority, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findByAssignedTo(String staffId) {
        return jpaRepository.findByAssignedTo(staffId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findByAssignedToAndStatus(String staffId, String status) {
        return jpaRepository.findByAssignedToAndStatus(staffId, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findByRequestedBy(String staffId) {
        return jpaRepository.findByRequestedBy(staffId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findAllWithPagination(int page, int size) {
        return jpaRepository.findAll(PageRequest.of(page, size))
                .getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Long countByStatus(String status) {
        return jpaRepository.countByStatus(status);
    }

    @Override
    public Long countByCleaningType(String cleaningType) {
        return jpaRepository.countByCleaningType(cleaningType);
    }

    @Override
    public Long countByPriority(String priority) {
        return jpaRepository.countByPriority(priority);
    }

    @Override
    public List<RoomCleaning> findActiveCleaning() {
        return jpaRepository.findActiveCleaning().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findOverdueCleaning(LocalDateTime currentDate) {
        return jpaRepository.findOverdueCleaning(currentDate).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findUrgentCleaning() {
        return jpaRepository.findUrgentCleaning().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCleaning> findByIsUrgent(Boolean isUrgent) {
        return jpaRepository.findByIsUrgent(isUrgent).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
