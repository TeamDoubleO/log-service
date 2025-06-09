package com.doubleo.logservice.domain.stats.service;

import com.doubleo.logservice.domain.log.repository.BuildingEnterLogRepository;
import com.doubleo.logservice.domain.stats.domain.DailyRetainedSnapshot;
import com.doubleo.logservice.domain.stats.domain.EntryStatsDaily;
import com.doubleo.logservice.domain.stats.domain.EntryStatsMonthly;
import com.doubleo.logservice.domain.stats.domain.EntryStatsWeekly;
import com.doubleo.logservice.domain.stats.dto.request.UpdateDailyEntryStatsRequest;
import com.doubleo.logservice.domain.stats.repository.DailyRetainedSnapshotRepository;
import com.doubleo.logservice.domain.stats.repository.EntryStatsDailyRepository;
import com.doubleo.logservice.domain.stats.repository.EntryStatsMonthlyRepository;
import com.doubleo.logservice.domain.stats.repository.EntryStatsWeeklyRepository;
import com.doubleo.logservice.global.enums.VisitCategory;
import com.doubleo.logservice.global.util.TenantValidator;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class StatsBatchServiceImpl implements StatsBatchService {

    private final BuildingEnterLogRepository buildingEnterLogRepository;
    private final EntryStatsDailyRepository entryStatsDailyRepository;
    private final EntryStatsWeeklyRepository entryStatsWeeklyRepository;
    private final EntryStatsMonthlyRepository entryStatsMonthlyRepository;
    private final DailyRetainedSnapshotRepository dailyRetainedSnapshotRepository;
    private final TenantValidator tenantValidator;

    public void updateDailyStats() {
        String tenantId = tenantValidator.getTenantId();
        LocalDate targetDate = LocalDate.now().minusDays(1);
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<UpdateDailyEntryStatsRequest> dtoList =
                buildingEnterLogRepository.countDailyGrouped(start, end, tenantId);

        List<EntryStatsDaily> stats =
                dtoList.stream()
                        .map(
                                dto ->
                                        new EntryStatsDaily(
                                                tenantId,
                                                targetDate,
                                                dto.buildingId(),
                                                dto.buildingName(),
                                                dto.visitCategory(),
                                                dto.enteredCount()))
                        .toList();

        entryStatsDailyRepository.saveAll(stats);
    }

    @Override
    public void updateWeeklyStats() {
        String tenantId = tenantValidator.getTenantId();

        LocalDate today = LocalDate.now();
        LocalDate thisMonday = today.with(DayOfWeek.MONDAY);
        LocalDate lastMonday = thisMonday.minusWeeks(1);
        LocalDate lastSunday = thisMonday.minusDays(1);

        LocalDateTime start = lastMonday.atStartOfDay();
        LocalDateTime end = thisMonday.atStartOfDay();

        Long count = buildingEnterLogRepository.countEnteredBetween(start, end, tenantId);

        EntryStatsWeekly weekly = new EntryStatsWeekly(tenantId, lastMonday, lastSunday, count);

        entryStatsWeeklyRepository.save(weekly);
    }

    @Override
    public void updateMonthlyStats() {
        String tenantId = tenantValidator.getTenantId();

        LocalDate today = LocalDate.now();
        LocalDate targetMonth = today.minusMonths(1);

        int year = targetMonth.getYear();
        int month = targetMonth.getMonthValue();

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1);

        Long count =
                buildingEnterLogRepository.countEnteredBetween(
                        startDate.atStartOfDay(), endDate.atStartOfDay(), tenantId);

        EntryStatsMonthly monthly = new EntryStatsMonthly(tenantId, year, month, count);

        entryStatsMonthlyRepository.save(monthly);
    }

    @Override
    public void saveDailyRetainedSnapshot() {
        String tenantId = tenantValidator.getTenantId();

        LocalDate snapshotDate = LocalDate.now().minusDays(1);
        LocalDateTime start = snapshotDate.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        for (VisitCategory category : VisitCategory.values()) {
            int base =
                    dailyRetainedSnapshotRepository
                            .findBySnapshotDateAndTenantIdAndVisitCategory(
                                    snapshotDate.minusDays(1), tenantId, category)
                            .map(DailyRetainedSnapshot::getRetainedCount)
                            .orElse(0);

            int inCount =
                    buildingEnterLogRepository.countEnteredByCategory(
                            start, end, tenantId, category);
            int outCount =
                    buildingEnterLogRepository.countExitedByCategory(
                            start, end, tenantId, category);
            int retained = base + inCount - outCount;

            DailyRetainedSnapshot snapshot =
                    new DailyRetainedSnapshot(tenantId, snapshotDate, category, retained);

            dailyRetainedSnapshotRepository.save(snapshot);
        }
    }
}
