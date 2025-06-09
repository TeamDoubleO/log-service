package com.doubleo.logservice.domain.stats.repository;

import com.doubleo.logservice.domain.stats.domain.EntryStatsMonthly;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EntryStatsMonthlyRepository extends JpaRepository<EntryStatsMonthly, Long> {
    @Query(
            """
    SELECT e
    FROM EntryStatsMonthly e
    WHERE e.tenantId = :tenantId
      AND (e.year < :currentYear OR (e.year = :currentYear AND e.month < :currentMonth))
    ORDER BY e.year DESC, e.month DESC
""")
    List<EntryStatsMonthly> findUpToPreviousMonth(
            String tenantId, int currentYear, int currentMonth);
}
