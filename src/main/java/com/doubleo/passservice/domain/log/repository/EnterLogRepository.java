package com.doubleo.passservice.domain.log.repository;

import com.doubleo.passservice.domain.log.domain.EnterLog;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterLogRepository extends JpaRepository<EnterLog, Long> {
    List<EnterLog> findAllByTenantId(String tenantId);

    Page<EnterLog> findAllByTenantId(String tenantId, Pageable pageable);
}
