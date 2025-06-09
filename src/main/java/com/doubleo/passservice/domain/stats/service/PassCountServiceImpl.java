package com.doubleo.passservice.domain.stats.service;

import com.doubleo.passservice.domain.pass.enums.VisitCategory;
import com.doubleo.passservice.domain.stats.dto.request.PassCountInfoRequest;
import com.doubleo.passservice.domain.stats.dto.response.PassCountInfoResponse;
import com.doubleo.passservice.domain.stats.repository.IssuedLogQueryRepository;
import com.doubleo.passservice.global.util.TenantValidator;
import com.doubleo.passservice.grpc.client.AreaClient;
import java.time.LocalDate;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PassCountServiceImpl implements PassCountService {

    private final IssuedLogQueryRepository issuedLogQueryRepository;
    private final TenantValidator tenantValidator;
    private final AreaClient areaClient;

    @Override
    public List<PassCountInfoResponse> getPassCount(PassCountInfoRequest request) {
        request.validatePeriod();
        request.validateDateRange();

        String tenantId = tenantValidator.getTenantId();
        LocalDate startDate = request.resolvedStartDate();
        LocalDate endDate = request.resolvedEndDate();
        List<String> areaCodes = request.areaCodes();
        List<VisitCategory> categories = request.categories();

        List<PassCountInfoResponse> result = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

            for (String areaCode : areaCodes) {
                long count =
                        issuedLogQueryRepository.countPassesByDateAndArea(
                                tenantId, areaCode, categories, date);

                String areaName;
                try {
                    areaName =
                            areaClient.getAreaFullNameByCode(tenantId, areaCode).getAreaFullName();
                } catch (Exception e) {
                    areaName = "";
                }

                result.add(new PassCountInfoResponse(date, areaCode, areaName, count));
            }
        }

        return result;
    }
}
