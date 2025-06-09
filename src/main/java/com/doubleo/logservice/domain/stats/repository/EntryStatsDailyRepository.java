package com.doubleo.logservice.domain.stats.repository;

import com.doubleo.logservice.domain.stats.domain.EntryStatsDaily;
import com.doubleo.logservice.domain.stats.dto.response.DailyStatsInfoListResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EntryStatsDailyRepository extends JpaRepository<EntryStatsDaily, Long> {
    @Query(
            """
    SELECT new com.doubleo.logservice.domain.stats.dto.response.DailyStatsInfoListResponse(e.date, SUM(e.entered))
    FROM EntryStatsDaily e
    WHERE e.tenantId = :tenantId
      AND e.date < :today
      AND e.date >= :startDate
    GROUP BY e.date
    ORDER BY e.date DESC
""")
    List<DailyStatsInfoListResponse> findDailyEnteredSumByDate(
            String tenantId, LocalDate today, LocalDate startDate);

    @Query(
            """
    SELECT e
    FROM EntryStatsDaily e
    WHERE e.tenantId = :tenantId
      AND e.date >= :startDate
      AND e.date < :endDate
    ORDER BY e.date DESC
""")
    List<EntryStatsDaily> findLastWeekStats(
            String tenantId, LocalDate startDate, LocalDate endDate);
}
