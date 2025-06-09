package com.doubleo.passservice.domain.stats.dto.request;

import com.doubleo.passservice.domain.pass.enums.VisitCategory;

public record UpdateDailyEntryStatsRequest(
        Long buildingId, String buildingName, VisitCategory visitCategory, Long enteredCount) {}
