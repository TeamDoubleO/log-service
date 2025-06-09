package com.doubleo.logservice.domain.stats.dto.response;

import com.doubleo.logservice.global.enums.VisitCategory;

public record RetainedStatusInfoResponse(
        VisitCategory category, int entered, int exited, int remaining) {}
