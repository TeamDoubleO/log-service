package com.doubleo.logservice.domain.log.dto.request;

import com.doubleo.logservice.global.enums.VisitCategory;

public record CreateAreaEnterLogRequest(
        String tenantId,
        Long areaId,
        Long memberId,
        String memberName,
        Long passId,
        VisitCategory visitCategory) {}
