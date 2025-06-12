package com.doubleo.logservice.grpc.server;

import com.doubleo.logservice.domain.log.domain.*;
import com.doubleo.logservice.domain.log.producer.AreaEnterLogStreamProducer;
import com.doubleo.logservice.domain.log.producer.BuildingEnterLogStreamProducer;
import com.doubleo.logservice.domain.log.repository.BuildingEnterLogRepository;
import com.doubleo.logservice.domain.log.repository.EnterLogRepository;
import com.doubleo.logservice.domain.log.repository.IssuedLogAreaRepository;
import com.doubleo.logservice.domain.log.repository.IssuedLogRepository;
import com.doubleo.logservice.global.enums.VisitCategory;
import com.doubleo.logservice.global.util.TimestampUtils;
import io.grpc.stub.StreamObserver;
import java.util.List;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class LogGrpcServiceImpl extends LogServiceGrpc.LogServiceImplBase {

    private final IssuedLogRepository issuedLogRepository;
    private final IssuedLogAreaRepository issuedLogAreaRepository;
    private final EnterLogRepository enterLogRepository;
    private final BuildingEnterLogRepository buildingEnterLogRepository;
    private final AreaEnterLogStreamProducer areaEnterLogProducer;
    private final BuildingEnterLogStreamProducer buildingEnterLogProducer;

    public LogGrpcServiceImpl(
            IssuedLogRepository issuedLogRepository,
            IssuedLogAreaRepository issuedLogAreaRepository,
            EnterLogRepository enterLogRepository,
            BuildingEnterLogRepository buildingEnterLogRepository,
            AreaEnterLogStreamProducer areaProducer,
            BuildingEnterLogStreamProducer buildingEnterLogProducer) {
        this.issuedLogRepository = issuedLogRepository;
        this.issuedLogAreaRepository = issuedLogAreaRepository;
        this.enterLogRepository = enterLogRepository;
        this.buildingEnterLogRepository = buildingEnterLogRepository;
        this.areaEnterLogProducer = areaProducer;

        this.buildingEnterLogProducer = buildingEnterLogProducer;
    }

    @Override
    public void createIssuedLog(
            CreateIssuedLogRequest request,
            StreamObserver<CreateIssuedLogResponse> responseObserver) {
        IssuedLog issuedLog =
                issuedLogRepository.save(
                        IssuedLog.createIssuedLog(
                                request.getTenantId(),
                                request.getMemberId(),
                                request.getMemberName(),
                                request.getMemberContact(),
                                request.getPassId(),
                                TimestampUtils.toLocalDateTime(request.getStartAt()),
                                TimestampUtils.toLocalDateTime(request.getExpiredAt()),
                                VisitCategory.valueOf(request.getVisitCategory())));
        List<IssuedLogArea> logAreas =
                request.getAreaCodesList().stream()
                        .map(
                                code ->
                                        IssuedLogArea.createIssuedLogArea(
                                                request.getTenantId(), issuedLog, code))
                        .toList();
        issuedLogAreaRepository.saveAll(logAreas);
        responseObserver.onNext(
                CreateIssuedLogResponse.newBuilder().setIssuedLogId(issuedLog.getId()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void createEnterLog(
            CreateEnterLogRequest request,
            StreamObserver<CreateEnterLogResponse> responseObserver) {
        areaEnterLogProducer.sendAreaEnterLogToStream(
                new com.doubleo.logservice.domain.log.dto.request.CreateAreaEnterLogRequest(
                        request.getTenantId(),
                        request.getAreaId(),
                        request.getMemberId(),
                        request.getMemberName(),
                        request.getPassId(),
                        VisitCategory.valueOf(request.getVisitCategory())));

        responseObserver.onNext(CreateEnterLogResponse.newBuilder().setEnterLogId(1L).build());
        responseObserver.onCompleted();
    }

    @Override
    public void createBuildingEnterLog(
            CreateBuildingEnterLogRequest request,
            StreamObserver<CreateBuildingEnterLogResponse> responseObserver) {
        buildingEnterLogProducer.sendBuildingEnterLogToStream(
                new com.doubleo.logservice.domain.log.dto.request.CreateBuildingEnterLogRequest(
                        request.getTenantId(),
                        request.getBuildingId(),
                        request.getMemberId(),
                        request.getMemberName(),
                        request.getPassId(),
                        Direction.valueOf(request.getDirection()),
                        VisitCategory.valueOf(request.getVisitCategory())));

        responseObserver.onNext(
                CreateBuildingEnterLogResponse.newBuilder().setBuildingEnterLogId(1L).build());
        responseObserver.onCompleted();
    }
}
