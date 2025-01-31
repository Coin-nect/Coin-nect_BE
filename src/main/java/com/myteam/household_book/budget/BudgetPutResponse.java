package com.myteam.household_book.budget;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetPutResponse {
    private boolean success;
    private int code;
    private String message;
    private Long budgetId;
    private Integer oldBudgetAmount;  // 기존 예산 금액
    private Integer newBudgetAmount;  // 새 예산 금액

    public BudgetPutResponse(boolean success, int code, String message, Long budgetId, Integer oldBudgetAmount, Integer newBudgetAmount) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.budgetId = budgetId;
        this.oldBudgetAmount = oldBudgetAmount;
        this.newBudgetAmount = newBudgetAmount;
    }

    public BudgetPutResponse(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
