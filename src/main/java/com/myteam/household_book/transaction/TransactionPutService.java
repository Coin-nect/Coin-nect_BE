package com.myteam.household_book.transaction;

import com.myteam.household_book.entity.Income;
import com.myteam.household_book.entity.Usage;
import com.myteam.household_book.repository.IncomeRepository;
import com.myteam.household_book.repository.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionPutService {

    @Autowired
    private UsageRepository usageRepository; //지출 내역 관리

    @Autowired
    private IncomeRepository incomeRepository; //수입 내역 관리

    // 트랜잭션 업데이트
    public TransactionPutResponse updateTransaction(TransactionPutRequest request, Long incomeId, Long usageId) {
        // 지출 ID가 제공된 경우
        if (usageId != null) {
            // 지출 항목 조회
            Usage usage = usageRepository.findById(usageId)
                    .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

            // 지출 항목 수정
            usage.setUsageTitle(request.getContent());
            usage.setUsageMemo(request.getMemo());
            usage.setUsagePrice(request.getPrice());
            usage.setUsageDate(request.getDate());
            usage.setUsageCategory(request.getCategoryId());

            usage = usageRepository.save(usage);
            return TransactionPutResponse.fromUsage(usage.getUsageId());
            // 수입 ID가 제공되었을 경우
        } else if (incomeId != null) {
            // 수입 항목 조회
            Income income = incomeRepository.findById(incomeId)
                    .orElseThrow(() -> new IllegalArgumentException("Income not found"));

            // 수입 항목 수정
            income.setIncomeTitle(request.getContent());
            income.setIncomeMemo(request.getMemo());
            income.setIncomePrice(request.getPrice());
            income.setIncomeDate(request.getDate());
            income.setIncomeCategory(request.getCategoryId());

            income = incomeRepository.save(income);
            return TransactionPutResponse.fromIncome(income.getIncomeId());
        } else {
            // 지출 ID나 수입 ID가 제공되지 않았을 경우 예외 처리
            throw new IllegalArgumentException("Invalid parameters provided");
        }
    }
}
