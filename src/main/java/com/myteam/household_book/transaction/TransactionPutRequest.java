package com.myteam.household_book.transaction;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionPutRequest {
    private Long userId;        // 사용자 ID
    private LocalDateTime date; // 날짜
    private Integer price;      // 금액
    private String content;     // 내용
    private String memo;        // 메모
    private String type;        // "expense" 또는 "income"
    private Integer categoryId; // 카테고리 ID
}
