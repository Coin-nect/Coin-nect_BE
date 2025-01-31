package com.myteam.household_book.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    // 1. 헤더에 년/월 표시 API
    @GetMapping("/api/calendar/header")
    public CalendarHeaderResponse getCalendarHeader(@RequestParam Long userId) {
        return calendarService.getCalendarHeader(userId);
    }

    // 2. 해당 달 캘린더 표시 API
    @GetMapping("/api/calendar/year/month")
    public CalendarMonthResponse getCalendarMonth(@RequestParam Long userId, @RequestParam int year, @RequestParam int month) {
        return calendarService.getCalendarMonth(userId, year, month);
    }

    // 3. 이전 달 캘린더 표시 API
    @GetMapping("/api/calendar/year/month/previous")
    public CalendarMonthResponse getPreviousMonth(@RequestParam Long userId, @RequestParam int year, @RequestParam int month) {
        return calendarService.getPreviousMonth(userId, year, month);
    }

    // 4. 다음 달 캘린더 표시 API
    @GetMapping("/api/calendar/year/month/next")
    public CalendarMonthResponse getNextMonth(@RequestParam Long userId, @RequestParam int year, @RequestParam int month) {
        return calendarService.getNextMonth(userId, year, month);
    }

}