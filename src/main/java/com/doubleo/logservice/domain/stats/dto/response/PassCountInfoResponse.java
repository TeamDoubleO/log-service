package com.doubleo.logservice.domain.stats.dto.response;

import java.time.LocalDate;

public record PassCountInfoResponse(
        LocalDate date, String areaCode, String areaName, long passCount) {}
