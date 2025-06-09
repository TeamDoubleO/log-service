package com.doubleo.logservice.domain.stats.domain;

import com.doubleo.logservice.domain.common.model.BaseEntity;
import com.doubleo.logservice.global.enums.VisitCategory;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(
        name = "entry_stats_daily",
        uniqueConstraints = {
            @UniqueConstraint(
                    columnNames = {
                        "tenant_id",
                        "entry_stats_daily_date",
                        "entry_stats_daily_building_id",
                        "entry_stats_daily_visit_category"
                    })
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class EntryStatsDaily extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entry_stats_daily_date", nullable = false)
    private LocalDate date;

    @Column(name = "entry_stats_daily_building_id", nullable = false)
    private Long buildingId;

    @Column(name = "entry_stats_daily_building_name", nullable = false)
    private String buildingName;

    @Column(name = "entry_stats_daily_visit_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitCategory visitCategory;

    @Column(name = "entry_stats_daily_entered", nullable = false)
    private Long entered;

    public EntryStatsDaily(
            String tenantId,
            LocalDate date,
            Long buildingId,
            String buildingName,
            VisitCategory visitCategory,
            Long entered) {
        this.tenantId = tenantId;
        this.date = date;
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.visitCategory = visitCategory;
        this.entered = entered;
    }
}
