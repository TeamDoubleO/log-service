package com.doubleo.passservice.domain.log.dto.response;

import java.time.LocalDateTime;

public record EnterLogResponse(
        String areaCode,
        String areaName,
        Long memberId,
        String memberName,
        Long passId,
        LocalDateTime createdDt) {}
