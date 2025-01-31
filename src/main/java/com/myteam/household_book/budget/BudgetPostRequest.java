package com.myteam.household_book.budget;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BudgetPostRequest {
    private Long userId;
    private Integer budgetAmount;
    private LocalDate currentDate; // 수정할 날짜 (현재 날짜 기준)
}
