package com.myteam.household_book.service;

import com.myteam.household_book.entity.Income;
import com.myteam.household_book.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class IncomeQueryService {

    private final IncomeRepository incomeRepo;

    public Page<Income> listByCategory(Long userId,
                                       int year, int month,
                                       Long categoryId,
                                       Pageable pageable) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end   = ym.plusMonths(1).atDay(1).atStartOfDay();

        Integer cat = (categoryId == null) ? null : categoryId.intValue();
        Pageable sortPage = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "incomeDate")
        );

        return incomeRepo.findByUserAndPeriodAndCategory(userId, start, end, cat, sortPage);
    }
}
