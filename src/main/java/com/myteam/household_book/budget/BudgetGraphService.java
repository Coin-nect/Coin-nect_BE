package com.myteam.household_book.budget;

import com.myteam.household_book.entity.Budget;
import com.myteam.household_book.entity.Usage;
import com.myteam.household_book.repository.BudgetRepository;
import com.myteam.household_book.repository.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetGraphService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UsageRepository usageRepository;

    public BudgetGraphResponse getBudgetGraph(Long userId) {
        List<BudgetGraphData> graphDataList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        int monthsToFetch = 5; // 최대 5개월치 데이터
        boolean hasPartialData = false; // 부분적인 데이터가 있는지 여부

        // 5개월치 데이터 조회
        for (int i = 0; i < monthsToFetch; i++) {
            YearMonth yearMonth = YearMonth.from(now.minusMonths(i)); // 현재부터 과거로 5개월
            LocalDate startDate = yearMonth.atDay(1); // 해당 월의 시작일
            LocalDate endDate = yearMonth.atEndOfMonth(); // 해당 월의 마지막일

            // 예산 데이터 조회
            Optional<Budget> budgetOpt = budgetRepository.findByUserIdAndDate(userId, startDate);
            int budgetAmount = budgetOpt.map(Budget::getBudgetAmount).orElse(0); // 예산이 없으면 0

            // 지출 데이터 조회
            List<Usage> usages = usageRepository.findByUserIdAndUsageDateYearAndUsageDateMonth(
                    userId, yearMonth.getYear(), yearMonth.getMonthValue());
            int expense = usages.stream().mapToInt(Usage::getUsagePrice).sum(); // 지출 합계

            // 그래프 데이터 추가
            graphDataList.add(new BudgetGraphData(yearMonth.toString(), budgetAmount, expense));

            // 예산이나 지출 데이터가 없는 경우 부분적인 데이터로 간주
            if (budgetAmount == 0 && expense == 0) {
                hasPartialData = true;
            }
        }

        // 부분적인 데이터가 있는 경우 메시지 추가
        String comment = hasPartialData ? "사용 가능한 데이터가 일부만 있습니다." : "5개월 모든 데이터를 조회했습니다.";

        // 정상적으로 5개월치 데이터가 있는 경우
        return new BudgetGraphResponse(true, 1000, "성공", comment, graphDataList);
    }
}