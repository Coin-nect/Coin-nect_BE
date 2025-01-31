/* 현재 달 캘린더 표시 API */


package com.myteam.household_book.calendar;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CalendarMonthResponse {

    private Boolean isSuccess;
    private int code;
    private String message;
    private Result result;

    @Getter
    @Setter
    public static class Result {
        private List<CalendarEntry> calendar;
    }

    @Getter
    @Setter
    public static class CalendarEntry {
        private String date;
        private int income;
        private int expense;

        // 생성자: date, income, expense 값을 초기화
        public CalendarEntry(String date, int income, int expense) {
            this.date = date;      // 파라미터로 받은 값을 this.date에 저장
            this.income = income;  // 파라미터로 받은 값을 this.income에 저장
            this.expense = expense; // 파라미터로 받은 값을 this.expense에 저장
        }
    }
}