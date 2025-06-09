package com.doubleo.passservice.domain.log.dto.request;

public record CreateAreaEnterLogRequest(
        String tenantId, Long areaId, Long memberId, String memberName, Long passId) {}
