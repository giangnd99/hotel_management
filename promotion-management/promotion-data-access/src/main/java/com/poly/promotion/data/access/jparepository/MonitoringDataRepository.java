package com.poly.promotion.data.access.jparepository;

import com.poly.promotion.data.access.jpaentity.MonitoringDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA repository for monitoring data entities.
 * Provides methods for data persistence and retrieval.
 *
 * @author System
 * @since 1.0.0
 */
@Repository
public interface MonitoringDataRepository extends JpaRepository<MonitoringDataEntity, Long> {

    /**
     * Finds all unsynced monitoring data.
     *
     * @return list of unsynced monitoring data
     */
    @Query("SELECT m FROM MonitoringDataEntity m WHERE m.synced = false ORDER BY m.createdAt ASC")
    List<MonitoringDataEntity> findUnsyncedData();

    /**
     * Finds monitoring data by type that hasn't been synced.
     *
     * @param dataType the type of monitoring data
     * @return list of unsynced monitoring data of the specified type
     */
    @Query("SELECT m FROM MonitoringDataEntity m WHERE m.dataType = :dataType AND m.synced = false ORDER BY m.createdAt ASC")
    List<MonitoringDataEntity> findUnsyncedDataByType(@Param("dataType") MonitoringDataEntity.MonitoringDataType dataType);

    /**
     * Finds old monitoring data for cleanup.
     *
     * @param cutoffDate the cutoff date for old data
     * @return list of old monitoring data
     */
    @Query("SELECT m FROM MonitoringDataEntity m WHERE m.createdAt < :cutoffDate")
    List<MonitoringDataEntity> findOldData(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Finds monitoring data by external ID.
     *
     * @param externalId the external ID
     * @return monitoring data entity or null if not found
     */
    @Query("SELECT m FROM MonitoringDataEntity m WHERE m.externalId = :externalId")
    MonitoringDataEntity findByExternalId(@Param("externalId") String externalId);

    /**
     * Counts unsynced monitoring data.
     *
     * @return count of unsynced data
     */
    @Query("SELECT COUNT(m) FROM MonitoringDataEntity m WHERE m.synced = false")
    long countUnsyncedData();

    /**
     * Counts monitoring data by type.
     *
     * @param dataType the type of monitoring data
     * @return count of data of the specified type
     */
    @Query("SELECT COUNT(m) FROM MonitoringDataEntity m WHERE m.dataType = :dataType")
    long countByDataType(@Param("dataType") MonitoringDataEntity.MonitoringDataType dataType);

    /**
     * Marks monitoring data as synced.
     *
     * @param id the ID of the monitoring data entity
     */
    @Modifying
    @Query("UPDATE MonitoringDataEntity m SET m.synced = true, m.lastSyncAttempt = :syncTime WHERE m.id = :id")
    void markAsSynced(@Param("id") Long id, @Param("syncTime") LocalDateTime syncTime);

    /**
     * Updates sync attempt information.
     *
     * @param id the ID of the monitoring data entity
     * @param syncAttempts the number of sync attempts
     * @param lastSyncAttempt the last sync attempt time
     * @param lastSyncError the last sync error message
     */
    @Modifying
    @Query("UPDATE MonitoringDataEntity m SET m.syncAttempts = :syncAttempts, m.lastSyncAttempt = :lastSyncAttempt, m.lastSyncError = :lastSyncError WHERE m.id = :id")
    void updateSyncAttemptInfo(@Param("id") Long id, 
                              @Param("syncAttempts") Integer syncAttempts,
                              @Param("lastSyncAttempt") LocalDateTime lastSyncAttempt,
                              @Param("lastSyncError") String lastSyncError);

    /**
     * Deletes old monitoring data.
     *
     * @param cutoffDate the cutoff date for old data
     * @return number of deleted records
     */
    @Modifying
    @Query("DELETE FROM MonitoringDataEntity m WHERE m.createdAt < :cutoffDate")
    int deleteOldData(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Deletes synced monitoring data older than the specified date.
     *
     * @param cutoffDate the cutoff date for old synced data
     * @return number of deleted records
     */
    @Modifying
    @Query("DELETE FROM MonitoringDataEntity m WHERE m.synced = true AND m.createdAt < :cutoffDate")
    int deleteOldSyncedData(@Param("cutoffDate") LocalDateTime cutoffDate);
}
