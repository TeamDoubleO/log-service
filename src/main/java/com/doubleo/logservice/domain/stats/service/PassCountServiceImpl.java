package com.doubleo.logservice.domain.stats.service;

import com.doubleo.logservice.domain.stats.dto.request.PassCountInfoRequest;
import com.doubleo.logservice.domain.stats.dto.response.PassCountInfoResponse;
import com.doubleo.logservice.domain.stats.repository.IssuedLogQueryRepository;
import com.doubleo.logservice.global.enums.VisitCategory;
import com.doubleo.logservice.global.util.TenantValidator;
import com.doubleo.logservice.grpc.client.AreaClient;
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

    @Override
    public List<PassCountInfoResponse> getMockPassCount() {
        String[] areaCodes = {"AA_01_01", "AA_02_01", "AB_01_02", "AC_02_02"};
        String[] areaNames = {"수면센터", "응급실", "의무기록실", "소아과"};
        int[][] counts = {
            {389, 1140, 160, 819},
            {342, 1251, 173, 890},
            {385, 1254, 160, 874},
            {379, 1165, 146, 857},
            {381, 1180, 163, 840},
            {355, 1119, 179, 836},
            {390, 1142, 165, 928},
            {396, 1204, 187, 899},
            {367, 1274, 177, 995},
            {345, 1273, 158, 871},
            {387, 1167, 161, 903},
            {343, 1167, 172, 844},
            {352, 1223, 180, 857},
            {395, 1260, 174, 952},
            {398, 1259, 177, 847},
            {358, 1136, 153, 966},
            {386, 1121, 176, 952},
            {391, 1128, 157, 910},
            {348, 1205, 180, 878},
            {358, 1213, 144, 931},
            {387, 1232, 174, 961},
            {360, 1283, 185, 988},
            {382, 1292, 165, 944},
            {349, 1294, 142, 822},
            {358, 1157, 171, 954},
            {389, 1114, 180, 989},
            {376, 1127, 178, 930},
            {348, 1292, 144, 838}
        };

        List<PassCountInfoResponse> result = new ArrayList<>();
        LocalDate start = LocalDate.now().minusDays(28);

        for (int i = 0; i < counts.length; i++) {
            for (int j = 0; j < areaCodes.length; j++) {
                result.add(
                        new PassCountInfoResponse(
                                start.plusDays(i), areaCodes[j], areaNames[j], counts[i][j]));
            }
        }

        return result;
    }
}
