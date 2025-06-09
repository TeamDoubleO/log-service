package com.doubleo.passservice.domain.stats.repository;

import com.doubleo.passservice.domain.pass.enums.VisitCategory;
import com.doubleo.passservice.domain.stats.domain.DailyRetainedSnapshot;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRetainedSnapshotRepository
        extends JpaRepository<DailyRetainedSnapshot, Long> {
    Optional<DailyRetainedSnapshot> findBySnapshotDateAndTenantIdAndVisitCategory(
            LocalDate snapshotDate, String tenantId, VisitCategory visitCategory);
}
