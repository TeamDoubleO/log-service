package com.doubleo.logservice.domain.log.repository;

import com.doubleo.logservice.domain.log.domain.IssuedLog;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IssuedLogRepository extends JpaRepository<IssuedLog, Long> {
    List<IssuedLog> findAllByTenantId(String tenantId);

    Page<IssuedLog> findAllByTenantId(String tenantId, Pageable pageable);

    @Query(
            """
        SELECT COUNT(il)
        FROM IssuedLog il
        WHERE il.createdDt >= :start
          AND il.createdDt < :end
          AND il.tenantId = :tenantId
    """)
    int countInLogsAtHour(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("tenantId") String tenantId);

    Page<IssuedLog> findAllByMemberNameAndTenantId(
            String memberName, String tenantId, Pageable pageable);
}
