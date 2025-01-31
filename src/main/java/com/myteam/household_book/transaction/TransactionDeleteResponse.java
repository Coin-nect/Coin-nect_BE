package com.myteam.household_book.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDeleteResponse {
    private Long id;
    private String message;

    // 수입 삭제 성공 메시지
    public static TransactionDeleteResponse fromIncome(Long incomeId) {
        return new TransactionDeleteResponse(incomeId, "Income Delete Success");
    }

    // 지출 삭제 성공 메시지
    public static TransactionDeleteResponse fromUsage(Long usageId) {
        return new TransactionDeleteResponse(usageId, "Usage Delete Success");
    }

    // 응답에서 key 이름을 income_id 또는 usage_id로 반환하는 메서드
    public String getResponseKey() {
        return (message.contains("Income")) ? "income_id" : "usage_id";
    }
}


