package com.doubleo.logservice.domain.log.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;

public record HourlyIssuanceResponse(
        int hour,
        int total,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
                LocalDateTime timestamp)
        implements Serializable {}
