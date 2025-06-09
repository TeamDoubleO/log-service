package com.doubleo.logservice.domain.log.dto.request;

import com.doubleo.logservice.domain.log.domain.Direction;
import com.doubleo.logservice.global.enums.VisitCategory;

public record CreateBuildingEnterLogRequest(
        String tenantId,
        Long buildingId,
        Long memberId,
        String memberName,
        Long passId,
        Direction direction,
        VisitCategory visitCategory) {}
