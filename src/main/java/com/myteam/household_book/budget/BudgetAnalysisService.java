/*
1. 이전달 지출내역이 없다면 "소비 분석이 여기 표시됩니다."
2. 이번달 지출내역이 10건 이하이라면 "저번 달에는 식비에 예산의 57%를 사용했어요." 띄워주기. 저번달 예산 없으면 없다고 띄워줌.
3. 이번달 지출내역이랑 저번달 지출내역비교해서 가장 변화 비율이 큰 카테고리에 대해 메세지
ex: 이번 달 미용/쇼핑에서 지출이 급증했습니다. 주의하세요! / 이번 달 식비 지출이 감소했습니다. 좋아요! 등
이때 50% 초과에 대해서는 급증했습니다. 50% 이하는 이번 달 식비 지출이 41% 증가했습니다. 로 */

package com.myteam.household_book.budget;

import com.myteam.household_book.entity.Budget;
import com.myteam.household_book.entity.Usage;
import com.myteam.household_book.entity.Category;
import com.myteam.household_book.repository.BudgetRepository;
import com.myteam.household_book.repository.CategoryRepository;
import com.myteam.household_book.repository.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BudgetAnalysisService {

    @Autowired
    private UsageRepository usageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    public BudgetAnalysisResponse analyzeSpending(Long userId) {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        // 이번 달 지출 내역 조회
        List<Usage> currentMonthUsages = usageRepository.findByUserIdAndUsageDateYearAndUsageDateMonth(
                userId, currentYear, currentMonth
        );

        // 저번 달 지출 내역 조회
        YearMonth lastMonth = YearMonth.of(currentYear, currentMonth).minusMonths(1);
        List<Usage> lastMonthUsages = usageRepository.findByUserIdAndUsageDateYearAndUsageDateMonth(
                userId, lastMonth.getYear(), lastMonth.getMonthValue()
        );

        // 이전달 지출내역이 없는 경우
        if (lastMonthUsages.isEmpty()) {
            return new BudgetAnalysisResponse(1000, "성공", "소비 분석이 여기 표시됩니다.", true);
        }

        if (currentMonthUsages.size() < 11) {
            // 저번 달 가장 큰 지출 비율 카테고리 찾기
            Map<Integer, Integer> lastMonthCategorySpending = new HashMap<>();
            for (Usage usage : lastMonthUsages) {
                lastMonthCategorySpending.put(
                        usage.getUsageCategory(),
                        lastMonthCategorySpending.getOrDefault(usage.getUsageCategory(), 0) + usage.getUsagePrice()
                );
            }

            int maxSpendingCategoryId = lastMonthCategorySpending.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(-1);

            if (maxSpendingCategoryId != -1) {
                Category category = categoryRepository.findById((long) maxSpendingCategoryId).orElse(null);
                if (category != null) {
                    // 해당 사용자의 저번 달 예산을 조회
                    Optional<Budget> budgetOptional = budgetRepository.findByUserIdAndDate(userId, lastMonth.atEndOfMonth());

                    if (budgetOptional.isPresent()) {
                        Budget budget = budgetOptional.get();
                        int budgetAmount = budget.getBudgetAmount(); // 저번 달 예산
                        int categorySpending = lastMonthCategorySpending.get(maxSpendingCategoryId); // 가장 많은 지출을 한 카테고리의 지출

                        // 예산의 비율 계산
                        double percentage = (double) categorySpending / budgetAmount * 100;
                        String resultMessage = String.format("저번 달에는 %s에 예산의 %.2f%%를 사용했어요.", category.getCategoryName(), percentage);
                        return new BudgetAnalysisResponse(1000, "성공", resultMessage, true);
                    } else {
                        return new BudgetAnalysisResponse(1000, "성공", "저번 달 예산 정보가 없습니다.", true);
                    }
                }
            }
        }

        // 이번 달과 저번 달 카테고리별 지출 비교
        Map<Integer, Integer> currentMonthCategorySpending = new HashMap<>();
        Map<Integer, Integer> lastMonthCategorySpending = new HashMap<>();

        for (Usage usage : currentMonthUsages) {
            currentMonthCategorySpending.put(
                    usage.getUsageCategory(),
                    currentMonthCategorySpending.getOrDefault(usage.getUsageCategory(), 0) + usage.getUsagePrice()
            );
        }

        for (Usage usage : lastMonthUsages) {
            lastMonthCategorySpending.put(
                    usage.getUsageCategory(),
                    lastMonthCategorySpending.getOrDefault(usage.getUsageCategory(), 0) + usage.getUsagePrice()
            );
        }

        // 가장 변화 비율이 큰 카테고리 찾기
        int maxChangeCategoryId = -1;
        double maxChangePercentage = 0;

        for (Map.Entry<Integer, Integer> entry : currentMonthCategorySpending.entrySet()) {
            int categoryId = entry.getKey();
            int currentSpending = entry.getValue();
            int lastSpending = lastMonthCategorySpending.getOrDefault(categoryId, 0);

            if (lastSpending == 0) continue;

            double changePercentage = ((double) (currentSpending - lastSpending) / lastSpending) * 100;
            if (Math.abs(changePercentage) > Math.abs(maxChangePercentage)) {
                maxChangePercentage = changePercentage;
                maxChangeCategoryId = categoryId;
            }
        }

        if (maxChangeCategoryId != -1) {
            Category category = categoryRepository.findById((long) maxChangeCategoryId).orElse(null);
            if (category != null) {
                String resultMessage;
                // 변화율이 50% 초과일 때는 급증 혹은 급감 메시지를 출력
                if (maxChangePercentage > 0) {  // 증가
                    if (Math.abs(maxChangePercentage) > 50) {
                        resultMessage = String.format("이번 달 %s 지출이 급증했습니다. 주의하세요!", category.getCategoryName());
                    } else {
                        resultMessage = String.format("이번 달 %s 지출이 %.2f%% 증가했습니다. 주의하세요!", category.getCategoryName(), Math.abs(maxChangePercentage));
                    }
                } else {  // 감소
                    if (Math.abs(maxChangePercentage) > 50) {
                        resultMessage = String.format("이번 달 %s 지출이 급감했습니다. 좋아요!", category.getCategoryName());
                    } else {
                        resultMessage = String.format("이번 달 %s 지출이 %.2f%% 감소했습니다. 좋아요!", category.getCategoryName(), Math.abs(maxChangePercentage));
                    }
                }
                return new BudgetAnalysisResponse(1000, "성공", resultMessage, true);
            }
        }

        return new BudgetAnalysisResponse(1000, "성공", "소비 분석이 여기 표시됩니다.", true); // 이전달 지출 내역이 없는 경우
    }
}