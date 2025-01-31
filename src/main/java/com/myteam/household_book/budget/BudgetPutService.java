package com.myteam.household_book.budget;

import com.myteam.household_book.entity.Budget;
import com.myteam.household_book.entity.User;
import com.myteam.household_book.repository.BudgetRepository;
import com.myteam.household_book.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

@Service
public class BudgetPutService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    // "currentDate": "2025-04-03" 형식으로 클라이언트로부터 현재 년월 받아서 처리
    public BudgetPutResponse updateBudget(Long budgetId, BudgetPutRequest request) {
        LocalDate currentDate = request.getCurrentDate();
        YearMonth yearMonth = YearMonth.from(currentDate);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();  // endDate는 항상 해당 월의 마지막 날짜로 설정됨

        Optional<Budget> budgetOpt = budgetRepository.findById(budgetId);
        if (budgetOpt.isEmpty()) {
            return new BudgetPutResponse(false, 1003, "예산을 찾을 수 없습니다.");
        }

        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            return new BudgetPutResponse(false, 1001, "사용자를 찾을 수 없습니다.");
        }

        Budget budget = budgetOpt.get();
        User user = userOpt.get();

        Optional<Budget> existingBudgetOpt = budgetRepository.findByUserIdAndDate(
                request.getUserId(), startDate);
        if (existingBudgetOpt.isPresent() && !existingBudgetOpt.get().getBudgetId().equals(budgetId)) {
            return new BudgetPutResponse(false, 1002, "이미 해당 기간에 예산이 등록되어 있습니다.");
        }

        // 기존 예산 금액
        Integer oldBudgetAmount = budget.getBudgetAmount();

        // 예산 정보 업데이트
        budget.setUserId(user);
        budget.setBudgetAmount(request.getBudgetAmount());
        budget.setStartDate(startDate);
        budget.setEndDate(endDate);  // endDate는 항상 해당 월의 마지막 날짜로 설정됨

        // 예산 저장
        budgetRepository.save(budget);

        // 새 예산 금액
        Integer newBudgetAmount = budget.getBudgetAmount();

        return new BudgetPutResponse(true, 1000, "예산이 성공적으로 수정되었습니다.", budget.getBudgetId(), oldBudgetAmount, newBudgetAmount);
    }
}
