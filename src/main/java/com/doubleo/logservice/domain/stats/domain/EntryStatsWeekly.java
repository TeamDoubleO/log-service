package com.doubleo.logservice.domain.stats.domain;

import com.doubleo.logservice.domain.common.model.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "entry_stats_weekly",
        uniqueConstraints = {
            @UniqueConstraint(
                    columnNames = {
                        "tenant_id",
                        "entry_stats_weekly_start_date",
                        "entry_stats_weekly_end_date"
                    })
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntryStatsWeekly extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entry_stats_weekly_start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "entry_stats_weekly_end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "entry_stats_weekly_entered", nullable = false)
    private Long entered;

    public EntryStatsWeekly(String tenantId, LocalDate startDate, LocalDate endDate, Long entered) {
        this.tenantId = tenantId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entered = entered;
    }
}
