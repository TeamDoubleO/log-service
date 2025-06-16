package com.doubleo.logservice.domain.log.service;

import com.doubleo.logservice.domain.log.dto.response.EnterLogResponse;
import com.doubleo.logservice.domain.log.dto.response.HourlyIssuanceResponse;
import com.doubleo.logservice.domain.log.dto.response.IssuedLogResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogService {
    Page<IssuedLogResponse> getAllIssuedLog(String keyword, Pageable pageable);

    Page<EnterLogResponse> getAllEnterLog(String keyword, Pageable pageable);

    List<HourlyIssuanceResponse> getHourlyIssuanceList();
}
