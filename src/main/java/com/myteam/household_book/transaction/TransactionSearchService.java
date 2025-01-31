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
public class TransactionSearchService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UsageRepository usageRepository;

    // 메모나 타이틀에 키워드가 포함된 모든 수입, 지출 내역을 검색
    public TransactionSearchResponse searchTransactions(Long userId, String keyword) {
        // 수입, 지출 내역 검색
        List<Income> incomeResults = incomeRepository.findByUserIdAndIncomeTitleContainingOrIncomeMemoContaining(userId, keyword);
        List<Usage> usageResults = usageRepository.findByUserIdAndUsageTitleContainingOrUsageMemoContaining(userId, keyword);

        // 결과가 없으면 "데이터가 없습니다" 메시지 반환
        if (incomeResults.isEmpty() && usageResults.isEmpty()) {
            return new TransactionSearchResponse(true, 1000, "데이터가 없습니다", new TransactionSearchResponse.Result(new ArrayList<>()));
        }

        // 수입 변환
        List<TransactionSearchResponse.Result.Transaction> incomeTransactions = incomeResults.stream()
                .map(income -> new TransactionSearchResponse.Result.Transaction(
                        income.getIncomePrice(),
                        income.getIncomeDate().toString(),
                        income.getIncomeCategory(),
                        income.getIncomeTitle(),
                        income.getIncomeMemo(),
                        "income"  // type은 "income"
                ))
                .collect(Collectors.toList());

        // 지출 변환
        List<TransactionSearchResponse.Result.Transaction> usageTransactions = usageResults.stream()
                .map(usage -> new TransactionSearchResponse.Result.Transaction(
                        usage.getUsagePrice(),
                        usage.getUsageDate().toString(),
                        usage.getUsageCategory(),
                        usage.getUsageTitle(),
                        usage.getUsageMemo(),
                        "expense"  // type은 "expense"
                ))
                .collect(Collectors.toList());

        // 수입과 지출 합치기
        List<TransactionSearchResponse.Result.Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(incomeTransactions);
        allTransactions.addAll(usageTransactions);

        // 날짜별로 정렬 (시간순)
        allTransactions.sort(Comparator.comparing(TransactionSearchResponse.Result.Transaction::getDate));

        // 결과 반환
        return new TransactionSearchResponse(true, 1000, "성공", new TransactionSearchResponse.Result(allTransactions));
    }
}

