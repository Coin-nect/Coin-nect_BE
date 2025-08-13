package com.myteam.household_book.service;

import com.myteam.household_book.dto.ListQuery;
import com.myteam.household_book.entity.Usage;
import com.myteam.household_book.repository.UsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class UsageQueryService {

    private final UsageRepository usageRepo;

    public Page<Usage> listByCategory(Long userId, ListQuery q) {
        YearMonth ym = YearMonth.of(q.year(), q.month());
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end   = ym.plusMonths(1).atDay(1).atStartOfDay();

        Pageable pageable = PageRequest.of(
                q.page(),
                q.size(),
                Sort.by(Sort.Direction.DESC, "usageDate")
        );

        return usageRepo.listByCategory(userId, start, end, q.categoryId(), pageable);
    }
}
