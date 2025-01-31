package com.myteam.household_book.budget;

import com.myteam.household_book.entity.Budget;
import com.myteam.household_book.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BudgetDeleteService {

    @Autowired
    private BudgetRepository budgetRepository;

    public BudgetDeleteResponse deleteBudget(Long budgetId) {
        // 예산 ID로 예산 조회
        Optional<Budget> budgetOpt = budgetRepository.findById(budgetId);
        if (budgetOpt.isEmpty()) {
            return new BudgetDeleteResponse(false, 1003, "예산을 찾을 수 없습니다.", budgetId);
        }

        // 예산 삭제
        budgetRepository.delete(budgetOpt.get());

        return new BudgetDeleteResponse(true, 1000, "예산이 성공적으로 삭제되었습니다.", budgetId);
    }
}
