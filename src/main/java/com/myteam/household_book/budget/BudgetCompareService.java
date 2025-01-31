// 증감률 API Service

package com.myteam.household_book.budget;

import com.myteam.household_book.entity.Budget;
import com.myteam.household_book.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

@Service
public class BudgetCompareService {

    @Autowired
    private BudgetRepository budgetRepository;

    public BudgetCompareResponse compareBudget(Long userId) {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        // 현재 달의 예산 조회
        Optional<Budget> currentBudgetOpt = budgetRepository.findByUserIdAndDate(
                userId,
                LocalDate.of(currentYear, currentMonth, 1)
        );

        // 저번 달의 예산 조회
        YearMonth lastMonth = YearMonth.of(currentYear, currentMonth).minusMonths(1);
        Optional<Budget> lastBudgetOpt = budgetRepository.findByUserIdAndDate(
                userId,
                LocalDate.of(lastMonth.getYear(), lastMonth.getMonthValue(), 1)
        );

        // 저번달 예산기록이 없는 경우
        String resultMessage = "예산 정보가 여기 표시됩니다.";

        if (currentBudgetOpt.isPresent() && lastBudgetOpt.isPresent()) {
            Budget currentBudget = currentBudgetOpt.get();
            Budget lastBudget = lastBudgetOpt.get();

            int currentAmount = currentBudget.getBudgetAmount();
            int lastAmount = lastBudget.getBudgetAmount();
            // 예산 변화율 메세지
            if (currentAmount > lastAmount) {
                double increasePercentage = ((double) (currentAmount - lastAmount) / lastAmount) * 100;
                resultMessage = String.format("저번 달보다 예산이 %.2f%% 증가했어요.", increasePercentage);
            } else if (currentAmount < lastAmount) {
                double decreasePercentage = ((double) (lastAmount - currentAmount) / lastAmount) * 100;
                resultMessage = String.format("저번 달보다 예산이 %.2f%% 감소했어요.", decreasePercentage);
            } else {
                resultMessage = "저번 달과 예산이 동일해요.";
            }
        }

        return new BudgetCompareResponse(true, 1000, "성공", resultMessage);
    }
}
