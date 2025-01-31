package com.myteam.household_book.budget;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetDeleteResponse {
    private boolean success;
    private int code;
    private String message;
    private Long budgetId;

    public BudgetDeleteResponse(boolean success, int code, String message, Long budgetId) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.budgetId = budgetId;
    }

}
