package com.doubleo.passservice.domain.log.repository;

import com.doubleo.passservice.domain.log.domain.IssuedLog;
import com.doubleo.passservice.domain.log.domain.IssuedLogArea;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IssuedLogAreaRepository extends JpaRepository<IssuedLogArea, Long> {
    @Query("SELECT i.areaCode FROM IssuedLogArea i WHERE i.issuedLog = :issuedLog")
    List<String> findAreaCodesByIssuedLog(@Param("issuedLog") IssuedLog issuedLog);
}
