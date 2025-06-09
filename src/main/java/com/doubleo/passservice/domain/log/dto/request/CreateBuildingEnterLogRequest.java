package com.doubleo.passservice.domain.log.dto.request;

import com.doubleo.passservice.domain.log.domain.Direction;
import com.doubleo.passservice.domain.pass.enums.VisitCategory;

public record CreateBuildingEnterLogRequest(
        String tenantId,
        Long buildingId,
        Long memberId,
        String memberName,
        Long passId,
        Direction direction,
        VisitCategory visitCategory) {}
