package com.myteam.household_book.service;

import com.myteam.household_book.dto.CategorySumDto;
import com.myteam.household_book.dto.MonthlyStatResponse;
import com.myteam.household_book.repository.CategoryRepository;
import com.myteam.household_book.repository.IncomeRepository;
import com.myteam.household_book.repository.UsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatService {

    private final UsageRepository usageRepo;
    private final IncomeRepository incomeRepo;
    private final CategoryRepository categoryRepo;

    @Cacheable(value = "monthlyStats", key = "#userId + ':' + #ym")
    public MonthlyStatResponse getMonthly(Long userId, YearMonth ym) {
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end   = ym.plusMonths(1).atDay(1).atStartOfDay();

        long expenseTotal = nvl(usageRepo.totalByMonth(userId, start, end));
        List<CategorySumDto> expenseRows = usageRepo.sumByCategory(userId, start, end);
        var expenseCats = toByCategory(expenseRows, expenseTotal);

        long incomeTotal = nvl(incomeRepo.totalByMonth(userId, start, end));
        List<CategorySumDto> incomeRows = incomeRepo.sumByCategory(userId, start, end);
        var incomeCats = toByCategory(incomeRows, incomeTotal);

        return new MonthlyStatResponse(
                ym.toString(),
                new MonthlyStatResponse.Side(expenseTotal, expenseCats),
                new MonthlyStatResponse.Side(incomeTotal, incomeCats)
        );
    }

    private List<MonthlyStatResponse.ByCategory> toByCategory(List<CategorySumDto> rows, long total) {
        // 1) 이름 매핑용 id 목록
        List<Long> ids = rows.stream()
                .map(r -> r.categoryId().longValue())
                .distinct()
                .toList();

        Map<Long, String> nameMap = categoryRepo.findAllById(ids).stream()
                .collect(Collectors.toMap(c -> c.getCategoryId(), c -> c.getCategoryName()));

        // 2) 응답 변환
        return rows.stream()
                .map(r -> {
                    Long id = r.categoryId().longValue();
                    Long amount = r.amount() == null ? 0L : r.amount();
                    String name = nameMap.getOrDefault(id, "기타");
                    double ratio = (total == 0L) ? 0d : amount.doubleValue() / (double) total;
                    return new MonthlyStatResponse.ByCategory(id, name, amount, ratio);
                })
                .toList();
    }


    private long nvl(Long v) { return v == null ? 0L : v; }
}
