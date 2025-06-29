package com.myteam.household_book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryStatsDto {
    private Integer categoryId;
    private String categoryName;
    private Long totalAmount;
    private Double percentage;
}