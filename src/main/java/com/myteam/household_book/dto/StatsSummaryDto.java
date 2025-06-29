package com.myteam.household_book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StatsSummaryDto {
    private List<CategoryStatsDto> incomeStats;
    private List<CategoryStatsDto> usageStats;
    private Long totalIncome;
    private Long totalUsage;
    private Long netAmount;
}