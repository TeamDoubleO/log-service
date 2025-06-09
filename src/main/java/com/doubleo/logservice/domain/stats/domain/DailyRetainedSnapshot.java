package com.doubleo.logservice.domain.stats.domain;

import com.doubleo.logservice.domain.common.model.BaseEntity;
import com.doubleo.logservice.global.enums.VisitCategory;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(
        name = "daily_retained_snapshot",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"snapshot_date", "tenant_id", "visit_category"})
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DailyRetainedSnapshot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snapshot_id")
    private Long id;

    @Column(name = "snapshot_date", nullable = false)
    private LocalDate snapshotDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_category", nullable = false)
    private VisitCategory visitCategory;

    @Column(name = "retained_count", nullable = false)
    private int retainedCount;

    public DailyRetainedSnapshot(
            String tenantId,
            LocalDate snapshotDate,
            VisitCategory visitCategory,
            int retainedCount) {
        this.tenantId = tenantId;
        this.snapshotDate = snapshotDate;
        this.visitCategory = visitCategory;
        this.retainedCount = retainedCount;
    }
}
