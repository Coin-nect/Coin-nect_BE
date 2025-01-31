package com.myteam.household_book.budget;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetCompareResponse {
    private boolean isSuccess;
    private int code;
    private String message;
    private String result;

    public BudgetCompareResponse(boolean isSuccess, int code, String message, String result) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
