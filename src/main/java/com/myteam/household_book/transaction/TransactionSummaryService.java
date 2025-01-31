//특정 달 총 자산, 수입, 지출 조회
package com.myteam.household_book.transaction;

import com.myteam.household_book.entity.Income;
import com.myteam.household_book.entity.Usage;
import com.myteam.household_book.repository.IncomeRepository;
import com.myteam.household_book.repository.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionSummaryService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UsageRepository usageRepository;

    // 해당 월의 수입, 지출, 자산 조회
    public TransactionSummaryResponse getTransactionSummary(Long userId, int year, int month) {
        List<Income> incomes = incomeRepository.findByUserIdAndIncomeDateYearAndIncomeDateMonth(userId, year, month);
        List<Usage> usages = usageRepository.findByUserIdAndUsageDateYearAndUsageDateMonth(userId, year, month);

        // 수입 합계 계산
        int totalIncome = incomes.stream().mapToInt(Income::getIncomePrice).sum();

        // 지출 합계 계산
        int totalExpense = usages.stream().mapToInt(Usage::getUsagePrice).sum();

        // 자산 계산 (수입 - 지출)
        int totalAssets = totalIncome - totalExpense;

        // 결과 반환
        return new TransactionSummaryResponse(true, 1000, "성공", new TransactionSummaryResponse.Result(totalIncome, totalExpense, totalAssets));
    }
}