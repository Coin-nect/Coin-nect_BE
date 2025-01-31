package com.myteam.household_book.budget;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BudgetPutRequest {
    private Long userId;          // 사용자 ID
    private Integer budgetAmount; // 수정할 예산 금액
    private LocalDate currentDate;  // 수정할 날짜 (현재 날짜 기준)
}
