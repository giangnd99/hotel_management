package com.poly.report.service;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    // Filter by period of time
    List<Object> filterByPeriodOfTime(String period, String reportType);

    // Filter by date range
    List<Object> filterByDate(LocalDate fromDate, LocalDate toDate, String reportType);

    Long getAvailableRoomCount();
    Long getTodayCheckInCount();
    Long getTodayCheckOutCount();
    Double getRevenueToday();
    List<Object> getRoomByFloor(Integer floorNumber);
    Object getRoomInfoById(Long roomId);
    Object updateRoomStatus(Long roomId, String status);
}