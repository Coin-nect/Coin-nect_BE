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

    public TransactionDeleteResponse deleteTransaction(Long incomeId, Long usageId) {
        if (usageId != null) {
            Usage usage = usageRepository.findById(usageId)
                    .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

            usageRepository.delete(usage);
            return new TransactionDeleteResponse(usageId, "Usage Delete Success");
        } else if (incomeId != null) {
            Income income = incomeRepository.findById(incomeId)
                    .orElseThrow(() -> new IllegalArgumentException("Income not found"));

            incomeRepository.delete(income);
            return new TransactionDeleteResponse(incomeId, "Income Delete Success");
        } else {
            throw new IllegalArgumentException("Invalid parameters provided");
        }
    }
}


