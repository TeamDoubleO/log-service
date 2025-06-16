package com.doubleo.logservice.domain.log.controller;

import com.doubleo.logservice.domain.log.dto.response.EnterLogResponse;
import com.doubleo.logservice.domain.log.dto.response.HourlyIssuanceResponse;
import com.doubleo.logservice.domain.log.dto.response.IssuedLogResponse;
import com.doubleo.logservice.domain.log.service.LogService;
import com.doubleo.logservice.global.util.TenantValidator;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pass-logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;
    private final TenantValidator tenantValidator;

    @Operation(summary = "Health Check API", description = "서비스 상태 확인 API")
    @GetMapping("/health")
    public String HealthCheck() {
        return "Log Service is healthy";
    }

    @Operation(summary = "All issued log get API", description = "모든 출입증 발급 로그 조회 API")
    @GetMapping("/issued")
    public Page<IssuedLogResponse> IssuedLogListGet(
            @RequestHeader("X-Admin-Id") Long adminId,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return logService.getAllIssuedLog(keyword, pageable);
    }

    @Operation(summary = "All issued log get API", description = "모든 출입 로그 조회 API")
    @GetMapping("/enter")
    public Page<EnterLogResponse> EnterLogListGet(
            @RequestHeader("X-Admin-Id") Long adminId,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return logService.getAllEnterLog(keyword, pageable);
    }

    @Operation(summary = "Number of issues per hour", description = "시간대별 출입증 발급 수 API")
    @GetMapping("/hourly-issuance")
    public List<HourlyIssuanceResponse> hourlyIssuanceListGet() {
        return logService.getHourlyIssuanceList();
    }
}
