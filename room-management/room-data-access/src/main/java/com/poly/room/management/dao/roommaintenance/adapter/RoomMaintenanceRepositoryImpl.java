package com.poly.room.management.dao.roommaintenance.adapter;

import com.poly.room.management.dao.roommaintenance.entity.RoomMaintenanceEntity;
import com.poly.room.management.dao.roommaintenance.mapper.RoomMaintenanceMapper;
import com.poly.room.management.dao.roommaintenance.repository.RoomMaintenanceJpaRepository;
import com.poly.room.management.domain.entity.RoomMaintenance;
import com.poly.room.management.domain.port.out.repository.RoomMaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoomMaintenanceRepositoryImpl implements RoomMaintenanceRepository {

    private final RoomMaintenanceJpaRepository jpaRepository;
    private final RoomMaintenanceMapper mapper;

    @Override
    public RoomMaintenance save(RoomMaintenance roomMaintenance) {
        RoomMaintenanceEntity entity = mapper.toEntity(roomMaintenance);
        RoomMaintenanceEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<RoomMaintenance> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<RoomMaintenance> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public RoomMaintenance update(RoomMaintenance roomMaintenance) {
        RoomMaintenanceEntity entity = mapper.toEntity(roomMaintenance);
        RoomMaintenanceEntity updatedEntity = jpaRepository.save(entity);
        return mapper.toDomain(updatedEntity);
    }

    @Override
    public List<RoomMaintenance> findByRoomId(UUID roomId) {
        return jpaRepository.findByRoom_RoomId(roomId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByRoomIdAndStatus(UUID roomId, String status) {
        return jpaRepository.findByRoomIdAndStatus(roomId, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByStatus(String status) {
        RoomMaintenanceEntity.MaintenanceStatus maintenanceStatus = RoomMaintenanceEntity.MaintenanceStatus.valueOf(status);
        return jpaRepository.findByStatus(maintenanceStatus).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByStatusWithPagination(String status, int page, int size) {
        return jpaRepository.findByStatusWithPagination(status, PageRequest.of(page, size))
                .getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByIssueType(String issueType) {
        RoomMaintenanceEntity.IssueType type = RoomMaintenanceEntity.IssueType.valueOf(issueType);
        return jpaRepository.findByIssueType(type).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByIssueTypeAndStatus(String issueType, String status) {
        return jpaRepository.findByIssueTypeAndStatus(issueType, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByPriority(String priority) {
        RoomMaintenanceEntity.MaintenancePriority maintenancePriority = RoomMaintenanceEntity.MaintenancePriority.valueOf(priority);
        return jpaRepository.findByPriority(maintenancePriority).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByPriorityAndStatus(String priority, String status) {
        return jpaRepository.findByPriorityAndStatus(priority, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByRequestedDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByRequestedDateBetween(startDate, endDate).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByScheduledDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByScheduledDateBetween(startDate, endDate).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByCompletedDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByCompletedDateBetween(startDate, endDate).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByAssignedTo(String staffId) {
        return jpaRepository.findByAssignedTo(staffId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByAssignedToAndStatus(String staffId, String status) {
        return jpaRepository.findByAssignedToAndStatus(staffId, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByRequestedBy(String staffId) {
        return jpaRepository.findByRequestedBy(staffId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByEstimatedCostGreaterThan(BigDecimal cost) {
        return jpaRepository.findByEstimatedCostGreaterThan(cost).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByActualCostBetween(BigDecimal minCost, BigDecimal maxCost) {
        return jpaRepository.findByActualCostBetween(minCost, maxCost).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findAllWithPagination(int page, int size) {
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
    public Long countByIssueType(String issueType) {
        return jpaRepository.countByIssueType(issueType);
    }

    @Override
    public Long countByPriority(String priority) {
        return jpaRepository.countByPriority(priority);
    }

    @Override
    public Long countByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.countByDateRange(startDate, endDate);
    }

    @Override
    public List<RoomMaintenance> findActiveMaintenance() {
        return jpaRepository.findActiveMaintenance().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findOverdueMaintenance(LocalDateTime currentDate) {
        return jpaRepository.findOverdueMaintenance(currentDate).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findUrgentMaintenance() {
        return jpaRepository.findUrgentMaintenance().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findCriticalMaintenance() {
        return jpaRepository.findCriticalMaintenance().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByIsUrgent(Boolean isUrgent) {
        return jpaRepository.findByIsUrgent(isUrgent).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalEstimatedCostByStatus(String status) {
        return jpaRepository.getTotalEstimatedCostByStatus(status);
    }

    @Override
    public BigDecimal getTotalActualCostByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.getTotalActualCostByDateRange(startDate, endDate);
    }

    @Override
    public List<RoomMaintenance> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findAllByScheduledAtBetween(startDate, endDate).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public List<RoomMaintenance> findByStaffId(String staffId) {
        return jpaRepository.findByRequestedBy(staffId).stream()
                .map(mapper::toDomain).toList();
    }
}
