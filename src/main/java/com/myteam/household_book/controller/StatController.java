package com.myteam.household_book.controller;

import com.myteam.household_book.dto.MonthlyStatResponse;
import com.myteam.household_book.security.CustomUserPrincipal;
import com.myteam.household_book.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatController {

    private final StatService statService;

    @GetMapping("/monthly")
    public MonthlyStatResponse monthly(@RequestParam Long userId,
                                       @RequestParam int year,
                                       @RequestParam int month) {
        return statService.getMonthly(userId, YearMonth.of(year, month));
    }

    // A) 쿼리로 userId 받는 버전
    @GetMapping("/monthly/income")
    public ResponseEntity<MonthlyStatResponse.Side> monthlyIncome(
            @RequestParam Long userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        var ym = YearMonth.of(year, month);
        var res = statService.getMonthly(userId, ym);
        return ResponseEntity.ok(res.income()); // income 블록만 반환
    }

    // B) JWT 적용 후: 로그인 사용자 기준
    @GetMapping("/me/monthly/income")
    public ResponseEntity<MonthlyStatResponse.Side> monthlyIncomeForMe(
            @AuthenticationPrincipal CustomUserPrincipal me,
            @RequestParam int year,
            @RequestParam int month
    ) {
        var ym = YearMonth.of(year, month);
        var res = statService.getMonthly(me.getId(), ym);
        return ResponseEntity.ok(res.income());
    }

    // 1) 개발/테스트용: userId 쿼리 파라미터로 받는 버전
    @GetMapping("/monthly/expense")
    public ResponseEntity<MonthlyStatResponse.Side> monthlyExpense(
            @RequestParam Long userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        YearMonth ym = YearMonth.of(year, month);
        MonthlyStatResponse res = statService.getMonthly(userId, ym);
        return ResponseEntity.ok(res.expense()); // ⭐ 지출 블록만 반환
    }

    // 2) 실제 서비스용: 로그인 사용자 기준(JWT)
    @GetMapping("/me/monthly/expense")
    public ResponseEntity<MonthlyStatResponse.Side> monthlyExpenseForMe(
            @AuthenticationPrincipal CustomUserPrincipal me,
            @RequestParam int year,
            @RequestParam int month
    ) {
        YearMonth ym = YearMonth.of(year, month);
        MonthlyStatResponse res = statService.getMonthly(me.getId(), ym);
        return ResponseEntity.ok(res.expense());
    }
}
