package com.doubleo.passservice.domain.log.controller;

import com.doubleo.passservice.domain.log.dto.request.UpdatePassStatusRequest;
import com.doubleo.passservice.domain.log.dto.response.*;
import com.doubleo.passservice.domain.log.service.LogService;
import com.doubleo.passservice.domain.pass.dto.response.PassCreateResponse;
import com.doubleo.passservice.domain.pass.service.PassService;
import com.doubleo.passservice.global.util.TenantValidator;
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
    private final PassService passService;
    private final TenantValidator tenantValidator;

    @Operation(summary = "All issued log get API", description = "모든 출입증 발급 로그 조회 API")
    @GetMapping("/issued")
    public Page<IssuedLogResponse> IssuedLogListGet(
            @RequestHeader("X-Admin-Id") Long adminId, Pageable pageable) {
        return logService.getAllIssuedLog(pageable);
    }

    @Operation(summary = "All issued log get API", description = "모든 출입 로그 조회 API")
    @GetMapping("/enter")
    public Page<EnterLogResponse> EnterLogListGet(
            @RequestHeader("X-Admin-Id") Long adminId, Pageable pageable) {
        return logService.getAllEnterLog(pageable);
    }

    @Operation(summary = "All pending pass get API", description = "모든 발급 대기중인 출입증 조회 API")
    @GetMapping("/pending")
    public Page<PendingPassResponse> PendingPassListGet(
            @RequestHeader("X-Admin-Id") Long adminId, Pageable pageable) {
        String tenantId = tenantValidator.getTenantId();
        return passService.getPendingPassList(tenantId, pageable);
    }

    @Operation(summary = "Accept Guardian application", description = "보호자 출입증 신청 승인 API")
    @PostMapping
    public PassCreateResponse GuardianApplicationCreate(
            @RequestHeader("X-Admin-Id") Long adminId,
            @RequestBody UpdatePassStatusRequest request) {
        return passService.createGuardianAndUpdatePassStatus(
                request.passId(), request.issuanceStatus());
    }

    @Operation(summary = "Number of issues per hour", description = "시간대별 출입증 발급 수 API")
    @GetMapping("/hourly-issuance")
    public List<HourlyIssuanceResponse> hourlyIssuanceListGet() {
        return logService.getHourlyIssuanceList();
    }
}
