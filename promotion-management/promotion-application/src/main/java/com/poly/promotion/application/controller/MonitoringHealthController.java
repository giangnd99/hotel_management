package com.poly.promotion.application.controller;

import com.poly.promotion.application.dto.response.hateoas.MonitoringHealthHateoasResponse;
import com.poly.promotion.application.service.HateoasLinkBuilderService;
import com.poly.promotion.application.service.MonitoringDataService;
import com.poly.promotion.application.service.MonitoringDataSyncService;
import com.poly.promotion.application.service.MonitoringDataCleanupService;
import com.poly.promotion.data.access.jpaentity.MonitoringDataEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.poly.promotion.application.annotation.LogBusinessOperation;
import com.poly.promotion.application.annotation.LogMethodEntry;
import com.poly.promotion.application.annotation.LogMethodError;
import com.poly.promotion.application.annotation.LogMethodExit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for monitoring data health checks and management.
 * Provides endpoints to check the status of local monitoring data storage and sync operations.
 *
 * @author System
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/monitoring/health")
@RequiredArgsConstructor
@Tag(name = "Monitoring Health", description = "Endpoints for monitoring data health checks and management")
public class MonitoringHealthController {

    private final MonitoringDataService monitoringDataService;
    private final MonitoringDataSyncService monitoringDataSyncService;
    private final MonitoringDataCleanupService monitoringDataCleanupService;
    private final HateoasLinkBuilderService hateoasLinkBuilderService;

    /**
     * Gets the overall health status of the monitoring data system.
     *
     * @return health status information
     */
    @GetMapping("/status")
    @Operation(summary = "Get monitoring data system health status", 
               description = "Returns comprehensive health information about local monitoring data storage and sync operations")
    @LogBusinessOperation(
        value = "Check monitoring system health",
        category = "MONITORING_HEALTH"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<MonitoringHealthHateoasResponse> getHealthStatus() {
        Map<String, Object> healthStatus = new HashMap<>();
        
        // Basic counts
        long totalRecords = monitoringDataService.countByDataType(null);
        long unsyncedRecords = monitoringDataService.countUnsyncedData();
        long syncedRecords = totalRecords - unsyncedRecords;
        
        healthStatus.put("status", "HEALTHY");
        healthStatus.put("totalRecords", totalRecords);
        healthStatus.put("unsyncedRecords", unsyncedRecords);
        healthStatus.put("syncedRecords", syncedRecords);
        healthStatus.put("syncPercentage", totalRecords > 0 ? (syncedRecords * 100.0 / totalRecords) : 0.0);
        
        // Data type breakdown
        Map<String, Long> dataTypeCounts = new HashMap<>();
        for (MonitoringDataEntity.MonitoringDataType dataType : MonitoringDataEntity.MonitoringDataType.values()) {
            long count = monitoringDataService.countByDataType(dataType);
            dataTypeCounts.put(dataType.name(), count);
        }
        healthStatus.put("dataTypeBreakdown", dataTypeCounts);
        
        // Sync statistics
        var syncStats = monitoringDataSyncService.getSyncStatistics();
        healthStatus.put("syncStatistics", syncStats);
        
        // Cleanup statistics
        var cleanupStats = monitoringDataCleanupService.getCleanupStatistics();
        healthStatus.put("cleanupStatistics", cleanupStats);
        
        // Overall assessment
        if (unsyncedRecords > 0 && totalRecords > 0) {
            double syncPercentage = (syncedRecords * 100.0 / totalRecords);
            if (syncPercentage < 80.0) {
                healthStatus.put("status", "DEGRADED");
                healthStatus.put("warning", "Low sync percentage: " + String.format("%.1f", syncPercentage) + "%");
            }
        }
        
        if (totalRecords > 10000) {
            healthStatus.put("status", "DEGRADED");
            healthStatus.put("warning", "High record count: " + totalRecords + " records");
        }
        
        // Create HATEOAS response
        MonitoringHealthHateoasResponse hateoasResponse = new MonitoringHealthHateoasResponse(healthStatus);
        hateoasResponse.add(hateoasLinkBuilderService.buildMonitoringHealthLinks());
        
        return ResponseEntity.ok(hateoasResponse);
    }

    /**
     * Gets detailed information about unsynced monitoring data.
     *
     * @return list of unsynced records
     */
    @GetMapping("/unsynced")
    @Operation(summary = "Get unsynced monitoring data", 
               description = "Returns detailed information about monitoring data that hasn't been synced to the external service")
    @LogBusinessOperation(
        value = "Retrieve unsynced monitoring data",
        category = "MONITORING_DATA"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<Map<String, Object>> getUnsyncedData() {
        List<MonitoringDataEntity> unsyncedData = monitoringDataService.getUnsyncedData();
        
        Map<String, Object> response = new HashMap<>();
        response.put("count", unsyncedData.size());
        response.put("records", unsyncedData);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Manually triggers a sync operation.
     *
     * @return sync operation result
     */
    @PostMapping("/sync/trigger")
    @Operation(summary = "Trigger manual sync", 
               description = "Manually triggers synchronization of unsynced monitoring data with the external service")
    @LogBusinessOperation(
        value = "Trigger manual sync",
        category = "MONITORING_SYNC"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<Map<String, Object>> triggerManualSync() {
        int syncedCount = monitoringDataSyncService.syncUnsyncedData();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Manual sync completed successfully");
        response.put("syncedRecords", syncedCount);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Manually triggers a cleanup operation.
     *
     * @return cleanup operation result
     */
    @PostMapping("/cleanup/trigger")
    @Operation(summary = "Trigger manual cleanup", 
               description = "Manually triggers cleanup of old monitoring data based on retention policies")
    @LogBusinessOperation(
        value = "Trigger manual cleanup",
        category = "MONITORING_CLEANUP"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<Map<String, Object>> triggerManualCleanup() {
        monitoringDataCleanupService.triggerManualCleanup();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Manual cleanup completed successfully");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Gets monitoring data statistics by type.
     *
     * @param dataType the type of monitoring data
     * @return statistics for the specified data type
     */
    @GetMapping("/stats/{dataType}")
    @Operation(summary = "Get monitoring data statistics by type", 
               description = "Returns statistics for monitoring data of a specific type")
    @LogBusinessOperation(
        value = "Get monitoring data statistics by type",
        category = "MONITORING_STATISTICS"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<Map<String, Object>> getDataTypeStatistics(
            @PathVariable String dataType) {
        MonitoringDataEntity.MonitoringDataType enumType;
        try {
            enumType = MonitoringDataEntity.MonitoringDataType.valueOf(dataType.toUpperCase());
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid data type: " + dataType);
            errorResponse.put("validTypes", MonitoringDataEntity.MonitoringDataType.values());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        long count = monitoringDataService.countByDataType(enumType);
        List<MonitoringDataEntity> unsyncedData = monitoringDataService.getUnsyncedDataByType(enumType);
        
        Map<String, Object> response = new HashMap<>();
        response.put("dataType", dataType);
        response.put("totalCount", count);
        response.put("unsyncedCount", unsyncedData.size());
        response.put("syncedCount", count - unsyncedData.size());
        response.put("syncPercentage", count > 0 ? ((count - unsyncedData.size()) * 100.0 / count) : 0.0);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}
