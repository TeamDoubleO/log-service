package com.doubleo.passservice.domain.stats.dto.response;

import com.doubleo.passservice.domain.pass.enums.VisitCategory;

public record RetainedStatusInfoResponse(
        VisitCategory category, int entered, int exited, int remaining) {}
