package com.myteam.household_book.transaction;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionPostRequest {
    private Long userId;
    private LocalDateTime date;
    private Integer price;
    private String content;
    private String memo;
    private String type; // "expense" 또는 "income"을 구분하는 필드 추가
    private Integer categoryId;
}
