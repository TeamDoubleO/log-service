package com.doubleo.logservice.domain.stats.dto.response;

import java.time.LocalDate;

public record WeeklyStatsInfoListResponse(LocalDate startDate, LocalDate endDate, Long entered) {
    public static WeeklyStatsInfoListResponse of(LocalDate start, LocalDate end, Long total) {
        return new WeeklyStatsInfoListResponse(start, end, total);
    }
}
