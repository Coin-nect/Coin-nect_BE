package com.myteam.household_book.dto;

import java.util.List;

public record MonthlyStatResponse(
        String period, // "2025-01"
        Side expense,
        Side income
) {
    public record Side(Long total, List<ByCategory> byCategory) {}
    public record ByCategory(Long categoryId, String name, Long amount, double ratio) {}
}
