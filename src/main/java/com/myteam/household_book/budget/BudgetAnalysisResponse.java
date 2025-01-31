package com.myteam.household_book.budget;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetAnalysisResponse {
    private int code;
    private String message;
    private String result;
    private boolean success;
}