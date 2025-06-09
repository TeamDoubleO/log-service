package com.doubleo.logservice.domain.log.dto.response;

import com.doubleo.logservice.domain.stats.dto.AreaInfo;
import com.doubleo.logservice.global.enums.VisitCategory;
import java.time.LocalDateTime;
import java.util.List;

public record IssuedLogResponse(
        Long memberId,
        String memberName,
        Long passId,
        List<AreaInfo> areas,
        LocalDateTime startAt,
        LocalDateTime expiredAt,
        VisitCategory visitCategory) {}
