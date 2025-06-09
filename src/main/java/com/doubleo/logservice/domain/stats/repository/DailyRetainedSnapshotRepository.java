package com.doubleo.logservice.domain.stats.repository;

import com.doubleo.logservice.domain.stats.domain.DailyRetainedSnapshot;
import com.doubleo.logservice.global.enums.VisitCategory;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRetainedSnapshotRepository
        extends JpaRepository<DailyRetainedSnapshot, Long> {
    Optional<DailyRetainedSnapshot> findBySnapshotDateAndTenantIdAndVisitCategory(
            LocalDate snapshotDate, String tenantId, VisitCategory visitCategory);
}
