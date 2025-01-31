// 특정 달 총 자산, 수입, 지출 조회
package com.myteam.household_book.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryResponse {
    private boolean isSuccess;
    private int code;
    private String message;
    private Result result;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private int totalIncome;
        private int totalExpense;
        private int totalAssets; //자산: 수입-지출
    }
}