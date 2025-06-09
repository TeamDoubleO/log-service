package com.doubleo.logservice.domain.stats.scheduler;

import com.doubleo.logservice.domain.stats.service.StatsBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EntryStatsScheduler {

    private final StatsBatchService statsBatchService;

    @Scheduled(cron = "0 10 0 * * *")
    public void runDailyStatsUpdate() {
        log.info("[EntryStatsScheduler] Starting daily entry stats aggregation");
        statsBatchService.updateDailyStats();
        log.info("[EntryStatsScheduler] Completed daily entry stats aggregation");
    }

    @Scheduled(cron = "0 20 0 * * MON")
    public void runWeeklyStatsUpdate() {
        log.info("[EntryStatsScheduler] Starting weekly entry stats aggregation");
        statsBatchService.updateWeeklyStats();
        log.info("[EntryStatsScheduler] Completed weekly entry stats aggregation");
    }

    @Scheduled(cron = "0 30 0 1 * *")
    public void runMonthlyStatsUpdate() {
        log.info("[EntryStatsScheduler] Starting monthly entry stats aggregation");
        statsBatchService.updateMonthlyStats();
        log.info("[EntryStatsScheduler] Completed monthly entry stats aggregation");
    }

    @Scheduled(cron = "0 5 0 * * *")
    public void runDailyRetainedSnapshotSave() {
        log.info("[EntryStatsScheduler] Starting daily retained snapshot save");
        statsBatchService.saveDailyRetainedSnapshot();
        log.info("[EntryStatsScheduler] Completed daily retained snapshot save");
    }
}
