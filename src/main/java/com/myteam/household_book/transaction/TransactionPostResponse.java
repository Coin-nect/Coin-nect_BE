package com.myteam.household_book.transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPostResponse {
    private Long id;
    private String message;

    // 수입 생성 성공 메시지
    public static TransactionPostResponse fromIncome(Long incomeId) {
        return new TransactionPostResponse(incomeId, "Income Created Successfully");
    }

    // 지출 생성 성공 메시지
    public static TransactionPostResponse fromUsage(Long usageId) {
        return new TransactionPostResponse(usageId, "Usage Created Successfully");
    }
}