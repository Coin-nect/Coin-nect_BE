/* 헤더에 년/월 표시하는 API */

package com.myteam.household_book.calendar;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarHeaderResponse {

    private Boolean isSuccess;
    private int code;
    private String message;
    private Result result;


    @Getter
    @Setter
    public static class Result {
        private int year;
        private int month;
        private String formattedDate;
    }
}