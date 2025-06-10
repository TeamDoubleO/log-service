package com.doubleo.logservice.domain.log.domain;

import com.doubleo.logservice.domain.common.model.BaseEntity;
import com.doubleo.logservice.global.enums.VisitCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "enter_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EnterLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enter_log_id")
    private Long id;

    @Column(name = "area_id", nullable = false)
    private Long areaId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "pass_id", nullable = false)
    private Long passId;

    @Column(name = "visit_category")
    @Enumerated(EnumType.STRING)
    private VisitCategory visitCategory;

    @Builder(access = AccessLevel.PRIVATE)
    private EnterLog(
            String tenantId,
            Long areaId,
            Long memberId,
            String memberName,
            Long passId,
            VisitCategory visitCategory) {
        this.tenantId = tenantId;
        this.areaId = areaId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.passId = passId;
        this.visitCategory = visitCategory;
    }

    public static EnterLog createEnterLog(
            String tenantId,
            Long areaId,
            Long memberId,
            String memberName,
            Long passId,
            VisitCategory visitCategory) {
        return EnterLog.builder()
                .tenantId(tenantId)
                .areaId(areaId)
                .memberId(memberId)
                .memberName(memberName)
                .passId(passId)
                .visitCategory(visitCategory)
                .build();
    }
}
