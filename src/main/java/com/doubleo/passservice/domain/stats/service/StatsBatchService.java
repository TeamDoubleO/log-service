package com.doubleo.passservice.domain.stats.service;

public interface StatsBatchService {

    void updateDailyStats();

    void updateWeeklyStats();

    void updateMonthlyStats();

    void saveDailyRetainedSnapshot();
}
