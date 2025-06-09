package com.doubleo.passservice.domain.stats.dto.response;

import java.time.LocalDate;

public record LastWeekBuildingStatsInfoListResponse(
        LocalDate date, String day, String buildingName, long total) {}
