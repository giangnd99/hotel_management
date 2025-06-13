package com.poly.report.controller;

import com.poly.report.model.RevenueReportModel;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/revenue-reports")
public class RevenueReportController {

    @GetMapping
    public List<RevenueReportModel> getAll() {
        return Arrays.asList(
                RevenueReportModel.builder()
                        .departmentId(1)
                        .totalRevenue(10000.0)
                        .totalCost(7000.0)
                        .totalProfit(3000.0)
                        .reportDate(LocalDate.now())
                        .build(),
                RevenueReportModel.builder()
                        .departmentId(2)
                        .totalRevenue(8000.0)
                        .totalCost(5000.0)
                        .totalProfit(3000.0)
                        .reportDate(LocalDate.now())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public RevenueReportModel getById(@PathVariable Integer id) {
        return RevenueReportModel.builder()
                .departmentId(id)
                .totalRevenue(12000.0)
                .totalCost(9000.0)
                .totalProfit(3000.0)
                .reportDate(LocalDate.now())
                .build();
    }

}
