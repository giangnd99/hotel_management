package com.poly.promotion.application.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.promotion.application.dto.monitoring.MethodExecutionMetrics;
import com.poly.promotion.application.dto.monitoring.PerformanceMetrics;
import com.poly.promotion.application.dto.monitoring.SystemHealthMetrics;
import com.poly.promotion.application.service.MonitoringDataService;
import com.poly.promotion.data.access.jpaentity.MonitoringDataEntity;
import com.poly.promotion.data.access.jparepository.MonitoringDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of MonitoringDataService.
 * Provides local storage for monitoring data to prevent data loss.
 *
 * @author System
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MonitoringDataServiceImpl implements MonitoringDataService {

    private final MonitoringDataRepository monitoringDataRepository;
    private final ObjectMapper objectMapper;

    @Override
    public MonitoringDataEntity saveMethodExecutionMetrics(MethodExecutionMetrics metrics) {
        try {
            String dataContent = objectMapper.writeValueAsString(metrics);
            
            MonitoringDataEntity entity = MonitoringDataEntity.builder()
                    .dataType(MonitoringDataEntity.MonitoringDataType.METHOD_EXECUTION_METRICS)
                    .externalId(metrics.getId())
                    .dataContent(dataContent)
                    .synced(false)
                    .syncAttempts(0)
                    .build();
            
            return monitoringDataRepository.save(entity);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize method execution metrics", e);
            throw new RuntimeException("Failed to save method execution metrics", e);
        }
    }

    @Override
    public MonitoringDataEntity savePerformanceMetrics(PerformanceMetrics metrics) {
        try {
            String dataContent = objectMapper.writeValueAsString(metrics);
            
            MonitoringDataEntity entity = MonitoringDataEntity.builder()
                    .dataType(MonitoringDataEntity.MonitoringDataType.PERFORMANCE_METRICS)
                    .externalId(metrics.getId())
                    .dataContent(dataContent)
                    .synced(false)
                    .syncAttempts(0)
                    .build();
            
            return monitoringDataRepository.save(entity);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize performance metrics", e);
            throw new RuntimeException("Failed to save performance metrics", e);
        }
    }

    @Override
    public MonitoringDataEntity saveSystemHealthMetrics(SystemHealthMetrics metrics) {
        try {
            String dataContent = objectMapper.writeValueAsString(metrics);
            
            MonitoringDataEntity entity = MonitoringDataEntity.builder()
                    .dataType(MonitoringDataEntity.MonitoringDataType.SYSTEM_HEALTH_METRICS)
                    .externalId(metrics.getId())
                    .dataContent(dataContent)
                    .synced(false)
                    .syncAttempts(0)
                    .build();
            
            return monitoringDataRepository.save(entity);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize system health metrics", e);
            throw new RuntimeException("Failed to save system health metrics", e);
        }
    }

    @Override
    public MonitoringDataEntity saveAuditLogEntry(String auditEntry) {
        MonitoringDataEntity entity = MonitoringDataEntity.builder()
                .dataType(MonitoringDataEntity.MonitoringDataType.AUDIT_LOG_ENTRY)
                .externalId(UUID.randomUUID().toString())
                .dataContent(auditEntry)
                .synced(false)
                .syncAttempts(0)
                .build();
        
        return monitoringDataRepository.save(entity);
    }

    @Override
    public MonitoringDataEntity saveErrorInfo(String errorInfo) {
        MonitoringDataEntity entity = MonitoringDataEntity.builder()
                .dataType(MonitoringDataEntity.MonitoringDataType.ERROR_INFO)
                .externalId(UUID.randomUUID().toString())
                .dataContent(errorInfo)
                .synced(false)
                .syncAttempts(0)
                .build();
        
        return monitoringDataRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitoringDataEntity> getUnsyncedData() {
        return monitoringDataRepository.findUnsyncedData();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitoringDataEntity> getUnsyncedDataByType(MonitoringDataEntity.MonitoringDataType dataType) {
        return monitoringDataRepository.findUnsyncedDataByType(dataType);
    }

    @Override
    public void markAsSynced(Long id) {
        monitoringDataRepository.markAsSynced(id, LocalDateTime.now());
    }

    @Override
    public void updateSyncAttemptInfo(Long id, String errorMessage) {
        MonitoringDataEntity entity = monitoringDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monitoring data not found: " + id));
        
        int newAttempts = entity.getSyncAttempts() + 1;
        monitoringDataRepository.updateSyncAttemptInfo(id, newAttempts, LocalDateTime.now(), errorMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MonitoringDataEntity> getById(Long id) {
        return monitoringDataRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MonitoringDataEntity> getByExternalId(String externalId) {
        return Optional.ofNullable(monitoringDataRepository.findByExternalId(externalId));
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnsyncedData() {
        return monitoringDataRepository.countUnsyncedData();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByDataType(MonitoringDataEntity.MonitoringDataType dataType) {
        return monitoringDataRepository.countByDataType(dataType);
    }

    @Override
    public int cleanupOldData(int retentionDays) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
        return monitoringDataRepository.deleteOldData(cutoffDate);
    }

    @Override
    public int cleanupOldSyncedData(int retentionDays) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
        return monitoringDataRepository.deleteOldSyncedData(cutoffDate);
    }
}
