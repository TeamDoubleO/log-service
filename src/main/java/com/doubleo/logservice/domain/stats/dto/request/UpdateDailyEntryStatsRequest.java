package com.doubleo.logservice.domain.stats.dto.request;

import com.doubleo.logservice.global.enums.VisitCategory;

public record UpdateDailyEntryStatsRequest(
        Long buildingId, String buildingName, VisitCategory visitCategory, Long enteredCount) {}
