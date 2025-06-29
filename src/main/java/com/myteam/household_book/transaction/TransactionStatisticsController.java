package com.myteam.household_book.transaction;

import com.myteam.household_book.dto.StatsSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/transaction/statistics")
@RequiredArgsConstructor  // 생성자 주입
public class TransactionStatisticsController {

    private final TransactionStatisticsService transactionStatisticsService;

    @GetMapping("/monthly/{userId}")
    public ResponseEntity<StatsSummaryDto> getMonthlyStats(
            @PathVariable Long userId,
            @RequestParam int year,
            @RequestParam int month) {

        StatsSummaryDto summary = transactionStatisticsService.getMonthlyIncomeStatistics(userId, year, month);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/range/{userId}")
    public ResponseEntity<StatsSummaryDto> getRangeStats(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        StatsSummaryDto summary = transactionStatisticsService.getIncomeStatistics(userId, startDate, endDate);
        return ResponseEntity.ok(summary);
    }
}