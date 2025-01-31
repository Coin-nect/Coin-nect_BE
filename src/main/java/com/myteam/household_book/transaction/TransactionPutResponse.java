package com.myteam.household_book.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionPutResponse {
    private Long id;
    private String message;

    // 기본 생성자
    public TransactionPutResponse() {}

    // Long과 String을 받는 생성자
    public TransactionPutResponse(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    // 수입 수정 성공 메시지
    public static TransactionPutResponse fromIncome(Long incomeId) {
        return new TransactionPutResponse(incomeId, "Income Updated Successfully");
    }

    // 지출 수정 성공 메시지
    public static TransactionPutResponse fromUsage(Long usageId) {
        return new TransactionPutResponse(usageId, "Usage Updated Successfully");
    }
}
