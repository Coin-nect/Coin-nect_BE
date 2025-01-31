// 오늘, 특정날짜 지출 및 수입 내역 조회
package com.myteam.household_book.transaction;

import lombok.AllArgsConstructor; //모든 매개변수가 있는 생성자 자동 생성
import lombok.Getter;
import lombok.NoArgsConstructor; //매개변수가 없는 생성자 자동 생성
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionGetResponse {
    private boolean isSuccess;
    private int code;
    private String message;
    private Result result;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private String date;
        private int totalIncome;
        private int totalExpense;
        private List<Transaction> transactions;
        private String comment;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Transaction {
            private int amount;
            private String date;
            private int category;
            private String title;
            private String memo;
            private String type; // income or usage
        }
    }
}
