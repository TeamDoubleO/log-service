package com.doubleo.logservice.domain.log.domain;

import com.doubleo.logservice.domain.common.model.BaseEntity;
import com.doubleo.logservice.global.enums.VisitCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "building_enter_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BuildingEnterLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_enter_log_id")
    private Long id;

    @Column(name = "building_id", nullable = false)
    private Long buildingId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "pass_id", nullable = false)
    private Long passId;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false, length = 10)
    private Direction direction;

    @Column(name = "visit_category")
    @Enumerated(EnumType.STRING)
    private VisitCategory visitCategory;

    @Builder(access = AccessLevel.PRIVATE)
    private BuildingEnterLog(
            String tenantId,
            Long buildingId,
            Long memberId,
            String memberName,
            Long passId,
            Direction direction,
            VisitCategory visitCategory) {
        this.tenantId = tenantId;
        this.buildingId = buildingId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.passId = passId;
        this.direction = direction;
        this.visitCategory = visitCategory;
    }

    public static BuildingEnterLog createBuildingEnterLog(
            String tenantId,
            Long buildingId,
            Long memberId,
            String memberName,
            Long passId,
            Direction direction,
            VisitCategory visitCategory) {
        return BuildingEnterLog.builder()
                .tenantId(tenantId)
                .buildingId(buildingId)
                .memberId(memberId)
                .memberName(memberName)
                .passId(passId)
                .direction(direction)
                .visitCategory(visitCategory)
                .build();
    }
}
