package com.doubleo.logservice.domain.stats.service;

import com.doubleo.logservice.domain.stats.dto.request.PassCountInfoRequest;
import com.doubleo.logservice.domain.stats.dto.response.PassCountInfoResponse;
import java.util.List;

public interface PassCountService {
    List<PassCountInfoResponse> getPassCount(PassCountInfoRequest request);

    List<PassCountInfoResponse> getMockPassCount();
}
