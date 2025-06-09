package com.doubleo.passservice.domain.log.repository;

import com.doubleo.passservice.domain.log.domain.BuildingEnterLog;
import com.doubleo.passservice.domain.stats.dto.request.UpdateDailyEntryStatsRequest;
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
    SELECT COUNT(*) FROM BuildingEnterLog l
    WHERE l.direction = 'IN'
    AND l.createdDt >= :start AND l.createdDt < :end
""")
    int countInLogsAtHour(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    Page<BuildingEnterLog> findAllByTenantId(String tenantId, Pageable pageable);

    @Query(
            """
    SELECT new com.doubleo.passservice.domain.stats.dto.request.UpdateDailyEntryStatsRequest(
        b.buildingId, b.memberName, p.visitCategory, COUNT(b)
    )
    FROM BuildingEnterLog b
    JOIN Pass p ON b.passId = p.id
    WHERE b.direction = 'IN'
      AND b.createdDt >= :start
      AND b.createdDt < :end
      AND b.tenantId = :tenantId
    GROUP BY b.buildingId, b.memberName, p.visitCategory
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
    JOIN Pass p ON b.passId = p.id
    WHERE b.direction = 'IN'
      AND b.createdDt >= :start
      AND b.createdDt < :end
      AND b.tenantId = :tenantId
      AND p.visitCategory = :visitCategory
""")
    int countEnteredByCategory(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("tenantId") String tenantId,
            @Param("visitCategory")
                    com.doubleo.passservice.domain.pass.enums.VisitCategory visitCategory);

    @Query(
            """
    SELECT COUNT(b)
    FROM BuildingEnterLog b
    JOIN Pass p ON b.passId = p.id
    WHERE b.direction = 'OUT'
      AND b.createdDt >= :start
      AND b.createdDt < :end
      AND b.tenantId = :tenantId
      AND p.visitCategory = :visitCategory
""")
    int countExitedByCategory(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("tenantId") String tenantId,
            @Param("visitCategory")
                    com.doubleo.passservice.domain.pass.enums.VisitCategory visitCategory);
}
