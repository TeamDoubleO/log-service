package com.doubleo.logservice.domain.log.repository;

import com.doubleo.logservice.domain.log.domain.BuildingEnterLog;
import com.doubleo.logservice.domain.stats.dto.request.UpdateDailyEntryStatsRequest;
import com.doubleo.logservice.global.enums.VisitCategory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BuildingEnterLogRepository extends JpaRepository<BuildingEnterLog, Long> {
    @Query(
            """
            SELECT COUNT(l) FROM BuildingEnterLog l
            WHERE l.direction = 'IN'
              AND l.createdDt >= :start
              AND l.createdDt < :end
              AND l.tenantId = :tenantId
            """)
    int countInLogsAtHour(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("tenantId") String tenantId);

    Page<BuildingEnterLog> findAllByTenantId(String tenantId, Pageable pageable);

    @Query(
            """
            SELECT new com.doubleo.logservice.domain.stats.dto.request.UpdateDailyEntryStatsRequest(
                b.buildingId, b.memberName, b.visitCategory, COUNT(b)
            )
            FROM BuildingEnterLog b
            WHERE b.direction = 'IN'
              AND b.createdDt >= :start
              AND b.createdDt < :end
              AND b.tenantId = :tenantId
            GROUP BY b.buildingId, b.memberName, b.visitCategory
            """)
    List<UpdateDailyEntryStatsRequest> countDailyGrouped(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("tenantId") String tenantId);

    @Query(
            """
    SELECT COUNT(b)
    FROM BuildingEnterLog b
    WHERE b.direction = 'IN'
      AND b.createdDt >= :start
      AND b.createdDt < :end
      AND b.tenantId = :tenantId
""")
    Long countEnteredBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("tenantId") String tenantId);

    @Query(
            """
            SELECT COUNT(b)
            FROM BuildingEnterLog b
            WHERE b.direction = 'IN'
              AND b.createdDt >= :start
              AND b.createdDt < :end
              AND b.tenantId = :tenantId
              AND b.visitCategory = :visitCategory
            """)
    int countEnteredByCategory(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("tenantId") String tenantId,
            @Param("visitCategory") VisitCategory visitCategory);

    @Query(
            """
            SELECT COUNT(b)
            FROM BuildingEnterLog b
            WHERE b.direction = 'OUT'
              AND b.createdDt >= :start
              AND b.createdDt < :end
              AND b.tenantId = :tenantId
              AND b.visitCategory = :visitCategory
            """)
    int countExitedByCategory(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("tenantId") String tenantId,
            @Param("visitCategory") VisitCategory visitCategory);
}
