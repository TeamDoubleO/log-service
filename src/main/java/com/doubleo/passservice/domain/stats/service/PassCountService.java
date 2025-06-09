package com.doubleo.passservice.domain.stats.service;

import com.doubleo.passservice.domain.stats.dto.request.PassCountInfoRequest;
import com.doubleo.passservice.domain.stats.dto.response.PassCountInfoResponse;
import java.util.List;

public interface PassCountService {
    List<PassCountInfoResponse> getPassCount(PassCountInfoRequest request);
}
