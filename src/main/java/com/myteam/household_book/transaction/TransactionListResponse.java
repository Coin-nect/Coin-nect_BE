// 리스트 버튼 API Response
package com.myteam.household_book.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionListResponse {
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
        private String comment;

        // 데이터가 없을 경우, 빈 리스트와 "데이터가 없습니다" 메시지를 설정
        public Result(String comment) {
            this.transactions = new ArrayList<>();
            this.comment = comment;
        }

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
            private String type; // "income" 또는 "usage"
        }
    }
}
