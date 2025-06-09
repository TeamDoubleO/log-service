package com.doubleo.passservice.domain.log.service;

import com.doubleo.passservice.domain.log.dto.response.EnterLogResponse;
import com.doubleo.passservice.domain.log.dto.response.HourlyIssuanceResponse;
import com.doubleo.passservice.domain.log.dto.response.IssuedLogResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogService {
    Page<IssuedLogResponse> getAllIssuedLog(Pageable pageable);

    Page<EnterLogResponse> getAllEnterLog(Pageable pageable);

    List<HourlyIssuanceResponse> getHourlyIssuanceList();
}
