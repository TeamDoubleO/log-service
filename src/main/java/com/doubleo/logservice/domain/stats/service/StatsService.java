package com.doubleo.logservice.domain.stats.service;

import com.doubleo.logservice.domain.log.dto.response.HourlyEntryResponse;
import com.doubleo.logservice.domain.stats.dto.response.*;
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
