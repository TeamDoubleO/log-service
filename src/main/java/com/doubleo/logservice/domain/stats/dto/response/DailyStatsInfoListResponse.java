package com.doubleo.logservice.domain.stats.dto.response;

import java.time.LocalDate;

public record DailyStatsInfoListResponse(LocalDate date, Long total) {
    public static DailyStatsInfoListResponse of(LocalDate date, Long total) {
        return new DailyStatsInfoListResponse(date, total);
    }

    public DailyStatsInfoListResponse(LocalDate date, Long total) {
        this.date = date;
        this.total = total;
    }
}
