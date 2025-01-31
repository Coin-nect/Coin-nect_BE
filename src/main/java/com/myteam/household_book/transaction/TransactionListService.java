// 리스트 버튼 API Service
package com.myteam.household_book.transaction;

import com.myteam.household_book.entity.Income;
import com.myteam.household_book.entity.Usage;
import com.myteam.household_book.repository.IncomeRepository;
import com.myteam.household_book.repository.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionListService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UsageRepository usageRepository;

    public TransactionListResponse getTransactionList(Long userId, int year, int month) {
        List<Income> incomes = incomeRepository.findByUserIdAndIncomeDateYearAndIncomeDateMonth(userId, year, month);
        List<Usage> usages = usageRepository.findByUserIdAndUsageDateYearAndUsageDateMonth(userId, year, month);

        // 수입 내역 변환
        List<TransactionListResponse.Result.Transaction> incomeTransactions = incomes.stream()
                .map(income -> new TransactionListResponse.Result.Transaction(income.getIncomePrice(), income.getIncomeDate().toString(), income.getIncomeCategory(), income.getIncomeTitle(), income.getIncomeMemo(), "income"))
                .collect(Collectors.toList());

        // 지출 내역 변환
        List<TransactionListResponse.Result.Transaction> usageTransactions = usages.stream()
                .map(usage -> new TransactionListResponse.Result.Transaction(usage.getUsagePrice(), usage.getUsageDate().toString(), usage.getUsageCategory(), usage.getUsageTitle(), usage.getUsageMemo(), "expense"))
                .collect(Collectors.toList());

        // 수입과 지출 리스트 합치기
        List<TransactionListResponse.Result.Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(incomeTransactions);
        allTransactions.addAll(usageTransactions);

        // 시간순 정렬
        allTransactions.sort(Comparator.comparing(TransactionListResponse.Result.Transaction::getDate));

        // 데이터가 없을 경우 처리
        if (allTransactions.isEmpty()) {
            return new TransactionListResponse(true, 1000, "성공", new TransactionListResponse.Result("데이터가 없습니다"));
        }

        // 결과 반환
        return new TransactionListResponse(true, 1000, "성공", new TransactionListResponse.Result(allTransactions, null));
    }
}

