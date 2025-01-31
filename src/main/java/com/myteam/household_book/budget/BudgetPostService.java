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
public class BudgetPostService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    public BudgetPostResponse addBudget(BudgetPostRequest request) {
        // // "currentDate": "2025-04-03" 형식으로 클라이언트로부터 현재 년월 받아서 처리
        LocalDate currentDate = request.getCurrentDate();

        // currentDate에서 해당 달의 startDate와 endDate를 계산
        YearMonth yearMonth = YearMonth.from(currentDate);
        LocalDate startDate = yearMonth.atDay(1);  // 해당 달의 첫 날
        LocalDate endDate = yearMonth.atEndOfMonth();  // 해당 달의 마지막 날

        // User 객체를 userId로 조회
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            return new BudgetPostResponse(false, 1001, "사용자를 찾을 수 없습니다.", null);
        }

        User user = userOpt.get(); // User 객체

        // 동일한 사용자가 동일한 기간에 이미 예산을 등록했는지 확인
        Optional<Budget> existingBudgetOpt = budgetRepository.findByUserIdAndDate(request.getUserId(), startDate);
        if (existingBudgetOpt.isPresent()) {
            return new BudgetPostResponse(false, 1002, "이미 해당 기간에 예산이 등록되어 있습니다.", null);
        }

        // 예산 객체 생성
        Budget budget = new Budget();
        budget.setUserId(user);  // User 객체를 setUserId 메소드에 설정
        budget.setBudgetAmount(request.getBudgetAmount());
        budget.setStartDate(startDate);
        budget.setEndDate(endDate);
        budget.setCreatedAt(LocalDate.now());

        // 예산을 저장하고, 예산 ID를 반환
        Budget savedBudget = budgetRepository.save(budget);

        return new BudgetPostResponse(true, 1000, "예산이 성공적으로 추가되었습니다.", savedBudget.getBudgetId());
    }
}
