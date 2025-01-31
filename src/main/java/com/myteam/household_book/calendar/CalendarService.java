package com.myteam.household_book.calendar;

import com.myteam.household_book.entity.Income;
import com.myteam.household_book.entity.Usage;
import com.myteam.household_book.repository.IncomeRepository;
import com.myteam.household_book.repository.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


@Service
public class CalendarService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UsageRepository usageRepository;

    // 1. 헤더 년/월 표시 API
    public CalendarHeaderResponse getCalendarHeader(Long userId) {
        LocalDate now = LocalDate.now();
        CalendarHeaderResponse response = new CalendarHeaderResponse();
        response.setIsSuccess(true);
        response.setCode(1000);
        response.setMessage("성공");

        CalendarHeaderResponse.Result result = new CalendarHeaderResponse.Result();
        result.setYear(now.getYear());
        result.setMonth(now.getMonthValue());
        result.setFormattedDate(now.getYear() + "년 " + now.getMonthValue() + "월");

        response.setResult(result);
        return response;
    }

    // 2. 해당 달 캘린더 조회 API
    public CalendarMonthResponse getCalendarMonth(Long userId, int year, int month) {
        // 수입과 지출 데이터를 해당 년도와 월에 맞게 조회
        List<Income> incomes = incomeRepository.findByUserIdAndIncomeDateYearAndIncomeDateMonth(userId, year, month);
        List<Usage> usages = usageRepository.findByUserIdAndUsageDateYearAndUsageDateMonth(userId, year, month);

        // 서비스 최종 응답 (DTO)
        CalendarMonthResponse response = new CalendarMonthResponse();
        response.setIsSuccess(true);
        response.setCode(1000);
        response.setMessage("성공");

        // 날짜별 수입과 지출을 합산할 Map 생성
        Map<String, CalendarMonthResponse.CalendarEntry> calendarMap = new HashMap<>();

        // 수입 데이터 합산
        for (Income income : incomes) {
            String date = income.getIncomeDate().toLocalDate().toString(); // 날짜 (LocalDate로 변환)
            int amount = income.getIncomePrice(); // 수입 금액

            // 해당 날짜에 이미 데이터가 있다면 합산, 없으면 새로 생성
            calendarMap.merge(date, new CalendarMonthResponse.CalendarEntry(date, amount, 0),
                    (existing, newEntry) -> new CalendarMonthResponse.CalendarEntry(existing.getDate(),
                            existing.getIncome() + newEntry.getIncome(),
                            existing.getExpense()));
        }

        // 지출 데이터 합산
        for (Usage usage : usages) {
            String date = usage.getUsageDate().toLocalDate().toString(); // 날짜 (LocalDate로 변환)
            int amount = usage.getUsagePrice(); // 지출 금액

            // 해당 날짜에 이미 데이터가 있다면 합산, 없으면 새로 생성
            calendarMap.merge(date, new CalendarMonthResponse.CalendarEntry(date, 0, amount),
                    (existing, newEntry) -> new CalendarMonthResponse.CalendarEntry(existing.getDate(),
                            existing.getIncome(),
                            existing.getExpense() + newEntry.getExpense()));
        }

        // Map의 값을 리스트로 변환
        List<CalendarMonthResponse.CalendarEntry> calendarEntries = new ArrayList<>(calendarMap.values());

        // 날짜순으로 정렬 (String으로 된 날짜를 LocalDate로 변환하여 비교)
        calendarEntries.sort((entry1, entry2) -> {
            LocalDate date1 = LocalDate.parse(entry1.getDate());
            LocalDate date2 = LocalDate.parse(entry2.getDate());
            return date1.compareTo(date2); // 날짜순으로 오름차순 정렬
        });

        // Result에 데이터 설정
        CalendarMonthResponse.Result result = new CalendarMonthResponse.Result();
        result.setCalendar(calendarEntries);
        response.setResult(result);

        return response;
    }

    // 3. 이전 달 캘린더 조회 API
    public CalendarMonthResponse getPreviousMonth(Long userId, int year, int month) {
        // 이전 달 계산
        LocalDate currentDate = LocalDate.of(year, month, 1);
        LocalDate previousMonth = currentDate.minusMonths(1);

        // getCalendarMonth 호출
        int previousYear = previousMonth.getYear();
        int previousMonthValue = previousMonth.getMonthValue();

        return getCalendarMonth(userId, previousYear, previousMonthValue);
    }

    // 4. 다음 달 캘린더 조회 API
    public CalendarMonthResponse getNextMonth(Long userId, int year, int month) {
        // 다음 달 계산
        LocalDate currentDate = LocalDate.of(year, month, 1);
        LocalDate nextMonth = currentDate.plusMonths(1);

        // getCalendarMonth 호출
        int nextYear = nextMonth.getYear();
        int nextMonthValue = nextMonth.getMonthValue();

        return getCalendarMonth(userId, nextYear, nextMonthValue);
    }
}

