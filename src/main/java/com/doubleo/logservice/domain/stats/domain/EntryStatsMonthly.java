package com.doubleo.logservice.domain.stats.domain;

import com.doubleo.logservice.domain.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "entry_stats_monthly",
        uniqueConstraints = {
            @UniqueConstraint(
                    columnNames = {
                        "tenant_id",
                        "entry_stats_monthly_year",
                        "entry_stats_monthly_month"
                    })
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntryStatsMonthly extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entry_stats_monthly_year", nullable = false)
    private int year;

    @Column(name = "entry_stats_monthly_month", nullable = false)
    private int month;

    @Column(name = "entry_stats_monthly_entered", nullable = false)
    private Long entered;

    public EntryStatsMonthly(String tenantId, int year, int month, Long entered) {
        this.tenantId = tenantId;
        this.year = year;
        this.month = month;
        this.entered = entered;
    }
}
