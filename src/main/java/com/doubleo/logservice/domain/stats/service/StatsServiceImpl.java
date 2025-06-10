package com.doubleo.logservice.domain.stats.service;

import com.doubleo.logservice.domain.log.dto.response.HourlyEntryResponse;
import com.doubleo.logservice.domain.log.repository.BuildingEnterLogRepository;
import com.doubleo.logservice.domain.stats.domain.DailyRetainedSnapshot;
import com.doubleo.logservice.domain.stats.domain.EntryStatsDaily;
import com.doubleo.logservice.domain.stats.dto.response.*;
import com.doubleo.logservice.domain.stats.repository.DailyRetainedSnapshotRepository;
import com.doubleo.logservice.domain.stats.repository.EntryStatsDailyRepository;
import com.doubleo.logservice.domain.stats.repository.EntryStatsMonthlyRepository;
import com.doubleo.logservice.domain.stats.repository.EntryStatsWeeklyRepository;
import com.doubleo.logservice.global.enums.VisitCategory;
import com.doubleo.logservice.global.util.TenantValidator;
import com.doubleo.tenantcontext.TenantContextHolder;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final EntryStatsDailyRepository entryStatsDailyRepository;
    private final EntryStatsWeeklyRepository entryStatsWeeklyRepository;
    private final EntryStatsMonthlyRepository entryStatsMonthlyRepository;
    private final BuildingEnterLogRepository buildingEnterLogRepository;
    private final DailyRetainedSnapshotRepository dailyRetainedSnapshotRepository;
    private final TenantValidator tenantValidator;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public List<HourlyEntryResponse> getHourlyEntryList() {
        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        String tenantId = tenantValidator.getTenantId();

        LocalDateTime end = now;
        LocalDateTime start = end.minusHours(24);

        List<HourlyEntryResponse> hourlyList = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            LocalDateTime hour = start.plusHours(i);
            String hourStr = hour.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH"));
            String redisKey = "hourlyEntry:" + tenantId + ":" + hourStr;

            String cachedCount = redisTemplate.opsForValue().get(redisKey);

            int total;
            if (cachedCount != null) {
                total = Integer.parseInt(cachedCount);
            } else {
                int count =
                        buildingEnterLogRepository.countInLogsAtHour(
                                hour, hour.plusHours(1), tenantId);
                redisTemplate
                        .opsForValue()
                        .set(redisKey, String.valueOf(count), Duration.ofSeconds(10));
                total = count;
            }

            hourlyList.add(new HourlyEntryResponse(hour.getHour(), total, hour));
        }

        return hourlyList;
    }

    @Override
    public List<DailyStatsInfoListResponse> getDailyPeriodStatsList() {

        String tenantId = tenantValidator.getTenantId();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(15);

        return entryStatsDailyRepository.findDailyEnteredSumByDate(tenantId, today, startDate);
    }

    @Override
    public List<WeeklyStatsInfoListResponse> getLastWeeksStatsList() {
        String tenantId = tenantValidator.getTenantId();
        LocalDate thisMonday = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endDate = thisMonday.minusDays(1);
        LocalDate startDate = thisMonday.minusWeeks(5);

        return entryStatsWeeklyRepository.findLastWeeks(tenantId, startDate, endDate).stream()
                .map(
                        weekly ->
                                WeeklyStatsInfoListResponse.of(
                                        weekly.getStartDate(),
                                        weekly.getEndDate(),
                                        weekly.getEntered()))
                .toList();
    }

    public List<MonthlyStatsInfoListResponse> getRecentMonthlyStatsList() {
        LocalDate now = LocalDate.now();
        return entryStatsMonthlyRepository
                .findUpToPreviousMonth(
                        tenantValidator.getTenantId(), now.getYear(), now.getMonthValue())
                .stream()
                .limit(12)
                .map(
                        e ->
                                MonthlyStatsInfoListResponse.of(
                                        e.getYear(), e.getMonth(), e.getEntered()))
                .toList();
    }

    public List<LastWeekCategoryStatsInfoListResponse> getLastWeekCategoryStats() {
        String tenantId = tenantValidator.getTenantId();
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today;

        List<EntryStatsDaily> stats =
                entryStatsDailyRepository.findLastWeekStats(tenantId, startDate, endDate);

        return stats.stream()
                .collect(
                        Collectors.groupingBy(
                                EntryStatsDaily::getDate,
                                Collectors.groupingBy(
                                        EntryStatsDaily::getVisitCategory,
                                        Collectors.summingLong(EntryStatsDaily::getEntered))))
                .entrySet()
                .stream()
                .flatMap(
                        entry -> {
                            LocalDate date = entry.getKey();
                            String day =
                                    date.getDayOfWeek()
                                            .getDisplayName(TextStyle.SHORT, Locale.KOREAN);
                            return entry.getValue().entrySet().stream()
                                    .map(
                                            catEntry ->
                                                    new LastWeekCategoryStatsInfoListResponse(
                                                            date,
                                                            day,
                                                            catEntry.getKey().name(),
                                                            catEntry.getValue()));
                        })
                .collect(Collectors.toList());
    }

    public List<LastWeekBuildingStatsInfoListResponse> getLastWeekBuildingStats() {
        String tenantId = tenantValidator.getTenantId();
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today;

        List<EntryStatsDaily> stats =
                entryStatsDailyRepository.findLastWeekStats(tenantId, startDate, endDate);

        return stats.stream()
                .collect(
                        Collectors.groupingBy(
                                EntryStatsDaily::getDate,
                                Collectors.groupingBy(
                                        EntryStatsDaily::getBuildingName,
                                        Collectors.summingLong(EntryStatsDaily::getEntered))))
                .entrySet()
                .stream()
                .flatMap(
                        dateEntry -> {
                            LocalDate date = dateEntry.getKey();
                            String day =
                                    date.getDayOfWeek()
                                            .getDisplayName(TextStyle.SHORT, Locale.KOREAN);

                            return dateEntry.getValue().entrySet().stream()
                                    .map(
                                            buildingEntry ->
                                                    new LastWeekBuildingStatsInfoListResponse(
                                                            date,
                                                            day,
                                                            buildingEntry.getKey(),
                                                            buildingEntry.getValue()));
                        })
                .collect(Collectors.toList());
    }

    public List<RetainedStatusInfoResponse> getCurrentRetainedStatus() {
        String tenantId = TenantContextHolder.getTenantId();
        LocalDate today = LocalDate.now();

        List<RetainedStatusInfoResponse> result = new ArrayList<>();

        for (VisitCategory category : VisitCategory.values()) {

            int base =
                    dailyRetainedSnapshotRepository
                            .findBySnapshotDateAndTenantIdAndVisitCategory(
                                    today, tenantId, category)
                            .map(DailyRetainedSnapshot::getRetainedCount)
                            .orElse(0);

            String inKey =
                    String.format("visit:count:%s:%s:%s:IN", tenantId, today, category.name());
            String outKey =
                    String.format("visit:count:%s:%s:%s:OUT", tenantId, today, category.name());

            String inVal = redisTemplate.opsForValue().get(inKey);
            String outVal = redisTemplate.opsForValue().get(outKey);

            int entered = (inVal != null && !inVal.isBlank()) ? Integer.parseInt(inVal) : 0;
            int exited = (outVal != null && !outVal.isBlank()) ? Integer.parseInt(outVal) : 0;
            int remaining = base + entered - exited;

            result.add(new RetainedStatusInfoResponse(category, entered, exited, remaining));
        }

        return result;
    }
}
