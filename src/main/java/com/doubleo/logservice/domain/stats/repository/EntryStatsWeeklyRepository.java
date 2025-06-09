package com.doubleo.logservice.domain.stats.repository;

import com.doubleo.logservice.domain.stats.domain.EntryStatsWeekly;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EntryStatsWeeklyRepository extends JpaRepository<EntryStatsWeekly, Long> {
    @Query(
            """
    SELECT e
    FROM EntryStatsWeekly e
    WHERE e.tenantId = :tenantId
      AND e.startDate >= :startDate
      AND e.endDate <= :endDate
    ORDER BY e.startDate DESC
""")
    List<EntryStatsWeekly> findLastWeeks(String tenantId, LocalDate startDate, LocalDate endDate);
}
