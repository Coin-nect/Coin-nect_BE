package com.myteam.household_book.dto.income;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class IncomeDtos {

    public record Create(
            @NotNull Long userId,                       // 지금은 외부에서 받음 (JWT 전환 시 제거)
            @NotNull @Size(min = 1, max = 255) String incomeTitle,
            @Size(max = 255) String incomeMemo,
            @NotNull @Min(0) Integer incomePrice,
            @NotNull LocalDateTime incomeDate,
            @NotNull Integer incomeCategory
    ) {}

    public record Update(
            @Size(min = 1, max = 255) String incomeTitle,
            @Size(max = 255) String incomeMemo,
            @Min(0) Integer incomePrice,
            LocalDateTime incomeDate,
            Integer incomeCategory
    ) {}

    public record Response(
            Long incomeId,
            Long userId,
            String incomeTitle,
            String incomeMemo,
            Integer incomePrice,
            LocalDateTime incomeDate,
            LocalDateTime createdAt,
            Integer incomeCategory
    ) {}
}
