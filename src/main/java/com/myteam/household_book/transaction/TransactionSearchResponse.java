package com.myteam.household_book.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSearchResponse {
    private boolean isSuccess;
    private int code;
    private String message;
    private Result result;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private List<Transaction> transactions;

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
            private String type;  // "income" or "expense"
        }
    }
}
