package com.myteam.household_book.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BudgetController {

    @Autowired
    private BudgetGetService budgetGetService;

    @Autowired
    private BudgetPostService budgetPostService;

    @Autowired
    private BudgetPutService budgetPutService;

    @Autowired
    private BudgetDeleteService budgetDeleteService;

    @Autowired
    private BudgetGraphService budgetGraphService;

    @Autowired
    private BudgetCompareService budgetService;

    @Autowired
    private BudgetAnalysisService budgetAnalysisService;

    // 1. 현재 년도/월에 대한 예산 정보만 조회
    @GetMapping("/api/budgets")
    public BudgetGetResponse getBudgetInfo(@RequestParam Long userId) {
        return budgetGetService.getBudgetInfo(userId);
    }

    // 2. 예산 추가
    @PostMapping("/api/budgets")
    public BudgetPostResponse addBudget(@RequestBody BudgetPostRequest request) {
        return budgetPostService.addBudget(request);
    }

    // 3. 예산 업데이트
    @PutMapping("/api/budgets")
    public BudgetPutResponse updateBudget(
            @RequestParam Long budgetId,
            @RequestBody BudgetPutRequest request) {
        return budgetPutService.updateBudget(budgetId, request);
    }

    // 4. 예산 삭제
    @DeleteMapping("/api/budgets")
    public BudgetDeleteResponse deleteBudget(@RequestParam Long budgetId) {
        return budgetDeleteService.deleteBudget(budgetId);
    }

    // 5. 예산 내역 그래프 데이터 조회
    @GetMapping("/api/budgets/graph")
    public BudgetGraphResponse getBudgetGraph(@RequestParam Long userId) {
        return budgetGraphService.getBudgetGraph(userId);
    }

    // 6. 예산 증감률 조회
    @GetMapping("/api/budgets/compare")
    public BudgetCompareResponse compareBudget(@RequestParam Long userId) {
        return budgetService.compareBudget(userId);
    }

    // 7. 소비 분석
    @GetMapping("/api/budgets/analyze")
    public BudgetAnalysisResponse analyzeSpending(@RequestParam Long userId) {
        return budgetAnalysisService.analyzeSpending(userId);
    }
}