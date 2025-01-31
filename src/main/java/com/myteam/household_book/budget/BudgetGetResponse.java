package com.myteam.household_book.budget;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetGetResponse {
    private boolean success;
    private int code;
    private String message;
    private Result result;

    @Getter
    @Setter
    public static class Result {
        private String yearMonth;
        private int budgetAmount;
        private int remainingBudget;
        private String warningLevel;
        private double spentRatio; // 지출 비율, 가로막대그래프에 사용
    }
}
