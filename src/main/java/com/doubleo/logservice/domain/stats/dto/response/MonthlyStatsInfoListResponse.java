package com.doubleo.logservice.domain.stats.dto.response;

public record MonthlyStatsInfoListResponse(int year, int month, long total) {
    public static MonthlyStatsInfoListResponse of(int year, int month, long total) {
        return new MonthlyStatsInfoListResponse(year, month, total);
    }
}
