package com.doubleo.logservice.domain.stats.dto.response;

import java.time.LocalDate;

public record LastWeekCategoryStatsInfoListResponse(
        LocalDate date, String day, String category, long total) {}
