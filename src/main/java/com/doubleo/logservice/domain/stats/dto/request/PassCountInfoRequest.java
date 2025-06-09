package com.doubleo.logservice.domain.stats.dto.request;

import com.doubleo.logservice.global.enums.VisitCategory;
import com.doubleo.logservice.global.exception.CommonException;
import com.doubleo.logservice.global.exception.errorcode.PassSearchErrorCode;
import java.time.LocalDate;
import java.util.List;

public record PassCountInfoRequest(
        List<VisitCategory> categories,
        Integer period,
        LocalDate startDate,
        LocalDate endDate,
        List<String> areaCodes) {
    public boolean isManualDate() {
        return period == null;
    }

    public LocalDate resolvedStartDate() {
        return isManualDate() ? startDate : LocalDate.now().minusDays(period);
    }

    public LocalDate resolvedEndDate() {
        return isManualDate() ? endDate : LocalDate.now().minusDays(1);
    }

    public void validatePeriod() {
        if (!isManualDate() && !List.of(7, 14, 28).contains(period)) {
            throw new CommonException(PassSearchErrorCode.INVALID_PERIOD);
        }
    }

    public void validateDateRange() {
        if (isManualDate()) {
            if (startDate == null || endDate == null) {
                throw new CommonException(PassSearchErrorCode.MISSING_DATE_IN_MANUAL_MODE);
            }
            if (startDate.isAfter(endDate)) {
                throw new CommonException(PassSearchErrorCode.INVALID_DATE_RANGE);
            }
        }
    }
}
