package com.doubleo.passservice.domain.stats.service;

import com.doubleo.passservice.domain.log.dto.response.HourlyEntryResponse;
import com.doubleo.passservice.domain.stats.dto.response.*;
import java.util.List;

public interface StatsService {

    List<HourlyEntryResponse> getHourlyEntryList();

    List<DailyStatsInfoListResponse> getDailyPeriodStatsList();

    List<WeeklyStatsInfoListResponse> getLastWeeksStatsList();

    List<MonthlyStatsInfoListResponse> getRecentMonthlyStatsList();

    List<LastWeekCategoryStatsInfoListResponse> getLastWeekCategoryStats();

    List<LastWeekBuildingStatsInfoListResponse> getLastWeekBuildingStats();

    List<RetainedStatusInfoResponse> getCurrentRetainedStatus();
}
