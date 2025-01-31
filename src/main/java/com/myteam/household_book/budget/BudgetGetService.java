package com.myteam.household_book.budget;

import com.myteam.household_book.entity.Budget;
import com.myteam.household_book.entity.Usage;
import com.myteam.household_book.repository.BudgetRepository;
import com.myteam.household_book.repository.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetGetService {

    @Autowired
    private UsageRepository usageRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    public BudgetGetResponse getBudgetInfo(Long userId) {
        BudgetGetResponse response = new BudgetGetResponse();
        BudgetGetResponse.Result result = new BudgetGetResponse.Result();

        // 현재 날짜 기준으로 연도와 월 계산
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        String yearMonth = year + "-" + String.format("%02d", month);  // "2025-01" 형식

        // 예산 정보 조회
        Optional<Budget> budgetOpt = budgetRepository.findByUserIdAndDate(userId, today);
        if (budgetOpt.isEmpty()) {
            // 예산을 입력하지 않은 경우에도 yearMonth 값 설정
            result.setYearMonth(yearMonth);
            response.setSuccess(true);
            response.setCode(1001);
            response.setMessage("예산을 입력하세요."); // 예산을 입력 안한 경우
            response.setResult(result);
            return response;
        }

        Budget budget = budgetOpt.get();

        // 해당 월의 지출 총액 계산
        List<Usage> usages = usageRepository.findByUserIdAndUsageDateYearAndUsageDateMonth(userId, year, month);
        int totalSpent = usages.stream().mapToInt(Usage::getUsagePrice).sum();

        // 남은 예산 계산
        int remainingBudget = budget.getBudgetAmount() - totalSpent;
        result.setBudgetAmount(budget.getBudgetAmount());
        result.setRemainingBudget(remainingBudget);

        // 경고 수준 계산
        int dayOfMonth = today.getDayOfMonth();
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        int dailyBudget = budget.getBudgetAmount() / daysInMonth;
        int expectedSpent = dailyBudget * dayOfMonth;
        double spentRatio = (double) totalSpent / expectedSpent;

        // budget_amount ÷ 해당 달의 일수 → 하루에 쓸 수 있는 예산 계산
        // 사용자가 예산 조회한 날짜까지 쓸 수 있는 금액(하루예산 × 일수)과 실제 사용 금액을 비교
        // 사용 비율에 따라 **여유(<= 0.3), 적정(> 0.3 ~ 0.75), 경고(> 0.75)**로 표시
        if (spentRatio <= 0.3) {
            result.setWarningLevel("여유");
        } else if (spentRatio <= 0.75) {
            result.setWarningLevel("적정");
        } else {
            result.setWarningLevel("경고");
        }

        result.setYearMonth(yearMonth);  // yearMonth 추가
        result.setSpentRatio(spentRatio); // spentRatio 추가 - 그래프에 사용

        response.setSuccess(true);
        response.setCode(1000);
        response.setMessage("성공");
        response.setResult(result);

        return response;
    }
}
