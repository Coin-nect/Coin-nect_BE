package com.myteam.household_book.transaction;

import com.myteam.household_book.entity.Usage;
import com.myteam.household_book.entity.Income;
import com.myteam.household_book.repository.UsageRepository;
import com.myteam.household_book.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionDeleteService {

    @Autowired
    private UsageRepository usageRepository;

    @Autowired
    private IncomeRepository incomeRepository;
    // 수입(incomeId) 또는 지출(usageId)을 삭제하는 메서드
    public TransactionDeleteResponse deleteTransaction(Long incomeId, Long usageId) {
        if (usageId != null) {
            // 지출 ID가 제공되면 해당 지출을 찾아서 삭제
            Usage usage = usageRepository.findById(usageId)
                    .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

            usageRepository.delete(usage);
            return new TransactionDeleteResponse(usageId, "Usage Delete Success");
        } else if (incomeId != null) {
            // 수입 ID가 제공되면 해당 수입을 찾아서 삭제
            Income income = incomeRepository.findById(incomeId)
                    .orElseThrow(() -> new IllegalArgumentException("Income not found"));

            incomeRepository.delete(income);
            return new TransactionDeleteResponse(incomeId, "Income Delete Success");
        } else {
            // 두 파라미터가 모두 제공되지 않으면 오류 발생
            throw new IllegalArgumentException("Invalid parameters provided");
        }
    }
}


