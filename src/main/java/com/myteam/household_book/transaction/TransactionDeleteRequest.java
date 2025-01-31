package com.myteam.household_book.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDeleteRequest {
    private Long usageId;  // 지출 ID
    private Long incomeId; // 수입 ID
    private String type;   // "expense" 또는 "income"
}