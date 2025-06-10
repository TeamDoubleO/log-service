package com.doubleo.logservice.domain.stats.repository;

import com.doubleo.logservice.global.enums.VisitCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IssuedLogQueryRepository {

    private final EntityManager em;

    public long countPassesByDateAndArea(
            String tenantId, String areaCode, List<VisitCategory> categories, LocalDate date) {

        if (categories == null || categories.isEmpty()) return 0L;

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        String jpql =
                """
            SELECT COUNT(il)
            FROM IssuedLogArea ila
            JOIN ila.issuedLog il
            WHERE ila.tenantId = :tenantId
              AND ila.areaCode = :areaCode
              AND il.visitCategory IN :categories
              AND il.createdDt <= :endOfDay
              AND il.createdDt >= :startOfDay
        """;

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("tenantId", tenantId);
        query.setParameter("areaCode", areaCode);
        query.setParameter("categories", categories);
        query.setParameter("startOfDay", startOfDay);
        query.setParameter("endOfDay", endOfDay);

        return query.getSingleResult();
    }
}
