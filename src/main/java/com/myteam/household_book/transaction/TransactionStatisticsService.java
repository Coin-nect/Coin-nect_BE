package com.myteam.household_book.transaction;

import com.myteam.household_book.dto.CategoryStatsDto;
import com.myteam.household_book.dto.StatsSummaryDto;
import com.myteam.household_book.entity.Category;
import com.myteam.household_book.repository.CategoryRepository;
import com.myteam.household_book.repository.IncomeRepository;
import com.myteam.household_book.repository.UsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor  // 생성자 주입으로 변경
public class TransactionStatisticsService {

    private final IncomeRepository incomeRepository;
    private final UsageRepository usageRepository;
    private final CategoryRepository categoryRepository;

    // 월별 통계 (Income만 사용하는 버전)
    @Transactional(readOnly = true)
    public StatsSummaryDto getMonthlyIncomeStatistics(Long userId, int year, int month) {

        // 카테고리 정보 조회 - Integer 타입으로 변환
        List<Category> categories = categoryRepository.findAll();
        Map<Integer, String> categoryMap = categories.stream()
                .collect(Collectors.toMap(
                        category -> category.getCategoryId().intValue(), // Long을 Integer로 변환
                        Category::getCategoryName));

        // 소득 통계만 조회
        List<Object[]> incomeRawStats = incomeRepository.findIncomeStatsByCategoryForMonth(userId, year, month);
        Long totalIncome = incomeRepository.findTotalIncomeByUserAndMonth(userId, year, month);
        if (totalIncome == null) totalIncome = 0L;

        // 지출은 일단 빈 리스트로 처리 (UsageRepository 메서드가 없으므로)
        List<CategoryStatsDto> usageStats = new ArrayList<>();
        Long totalUsage = 0L;

        // 소득 통계 변환
        List<CategoryStatsDto> incomeStats = convertToStatsDto(incomeRawStats, categoryMap, totalIncome);

        Long netAmount = totalIncome - totalUsage;

        return new StatsSummaryDto(incomeStats, usageStats, totalIncome, totalUsage, netAmount);
    }

    // 날짜 범위 통계 (Income만 사용하는 버전)
    @Transactional(readOnly = true)
    public StatsSummaryDto getIncomeStatistics(Long userId,
                                               LocalDateTime startDate,
                                               LocalDateTime endDate) {

        List<Category> categories = categoryRepository.findAll();
        Map<Integer, String> categoryMap = categories.stream()
                .collect(Collectors.toMap(
                        category -> category.getCategoryId().intValue(),
                        Category::getCategoryName));

        List<Object[]> incomeRawStats = incomeRepository.findIncomeStatsByCategory(userId, startDate, endDate);
        Long totalIncome = incomeRepository.findTotalIncomeByUserAndDateRange(userId, startDate, endDate);
        if (totalIncome == null) totalIncome = 0L;

        // 지출은 일단 빈 리스트로 처리
        List<CategoryStatsDto> usageStats = new ArrayList<>();
        Long totalUsage = 0L;

        List<CategoryStatsDto> incomeStats = convertToStatsDto(incomeRawStats, categoryMap, totalIncome);

        Long netAmount = totalIncome - totalUsage;

        return new StatsSummaryDto(incomeStats, usageStats, totalIncome, totalUsage, netAmount);
    }

    private List<CategoryStatsDto> convertToStatsDto(List<Object[]> rawStats,
                                                     Map<Integer, String> categoryMap,
                                                     Long total) {
        List<CategoryStatsDto> result = new ArrayList<>();

        for (Object[] row : rawStats) {
            Integer categoryId = (Integer) row[0];
            Long amount = (Long) row[1];
            String categoryName = categoryMap.getOrDefault(categoryId, "Unknown");
            Double percentage = total > 0 ? (amount.doubleValue() / total.doubleValue() * 100) : 0.0;

            result.add(new CategoryStatsDto(categoryId, categoryName, amount, percentage));
        }

        return result;
    }
}