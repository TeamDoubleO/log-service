package com.doubleo.logservice.domain.log.domain;

import com.doubleo.logservice.domain.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "issued_log_area")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssuedLogArea extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issued_log_area_id")
    private Long id;

    @JoinColumn(name = "issued_log_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private IssuedLog issuedLog;

    @Column(name = "area_code", nullable = false)
    private String areaCode;

    @Builder(access = AccessLevel.PRIVATE)
    private IssuedLogArea(String tenantId, IssuedLog issuedLog, String areaCode) {
        this.tenantId = tenantId;
        this.issuedLog = issuedLog;
        this.areaCode = areaCode;
    }

    public static IssuedLogArea createIssuedLogArea(
            String tenantId, IssuedLog issuedLog, String areaCode) {
        return IssuedLogArea.builder()
                .tenantId(tenantId)
                .issuedLog(issuedLog)
                .areaCode(areaCode)
                .build();
    }
}
