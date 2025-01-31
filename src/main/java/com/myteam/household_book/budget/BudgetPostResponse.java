package com.myteam.household_book.budget;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetPostResponse {
    private boolean success;
    private int code;
    private String message;
    private Long budgetId; // 예산의 ID

    public BudgetPostResponse(boolean success, int code, String message, Long budgetId) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.budgetId = budgetId;
    }

}
