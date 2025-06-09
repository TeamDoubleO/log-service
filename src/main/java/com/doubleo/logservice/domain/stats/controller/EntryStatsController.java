package com.doubleo.logservice.domain.stats.controller;

import com.doubleo.logservice.domain.log.dto.response.HourlyEntryResponse;
import com.doubleo.logservice.domain.stats.dto.response.*;
import com.doubleo.logservice.domain.stats.service.StatsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pass-logs")
@RequiredArgsConstructor
public class EntryStatsController {

    private final StatsService statsService;

    @GetMapping("/hourly")
    public List<HourlyEntryResponse> hourlyEntryListGet() {
        return statsService.getHourlyEntryList();
    }

    @GetMapping("/period/daily")
    public List<DailyStatsInfoListResponse> dailyPeriodStatsListGet() {
        return statsService.getDailyPeriodStatsList();
    }

    @GetMapping("/period/weekly")
    public List<WeeklyStatsInfoListResponse> weeklyPeriodStatsListGet() {
        return statsService.getLastWeeksStatsList();
    }

    @GetMapping("/period/monthly")
    public List<MonthlyStatsInfoListResponse> monthlyPeriodStatsListGet() {
        return statsService.getRecentMonthlyStatsList();
    }

    @GetMapping("/category")
    public List<LastWeekCategoryStatsInfoListResponse> lastWeekCategoryStatsListGet() {
        return statsService.getLastWeekCategoryStats();
    }

    @GetMapping("/building")
    public List<LastWeekBuildingStatsInfoListResponse> lastWeekBuildingStatsListGet() {
        return statsService.getLastWeekBuildingStats();
    }

    @GetMapping("/dashboard-summary")
    public List<RetainedStatusInfoResponse> currentRetainedStatusGet() {
        return statsService.getCurrentRetainedStatus();
    }
}
