/* 예산 및 지출 데이터를 저장하는 DTO */
package com.myteam.household_book.budget;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetGraphData {
    private String month; // 월 (예: "2024-09")
    private int budgetAmount; // 예산 금액
    private int expense; // 지출 금액

    public BudgetGraphData(String month, int budgetAmount, int expense) {
        this.month = month;
        this.budgetAmount = budgetAmount;
        this.expense = expense;
    }
}