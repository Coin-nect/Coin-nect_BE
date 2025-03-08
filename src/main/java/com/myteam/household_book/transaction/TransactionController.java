package com.myteam.household_book.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionGetService transactionService; // 서비스 주입

    @Autowired
    private TransactionSummaryService transactionSummaryService; // 서비스 주입

    @Autowired
    private TransactionListService transactionListService; // 서비스 주입

    @Autowired
    private TransactionSearchService transactionSearchService; // 서비스 주입

    @Autowired
    private TransactionPostService transactionPostService;

    @Autowired
    private TransactionDeleteService transactionDeleteService;

    @Autowired
    private TransactionPutService transactionPutService; // 서비스 주입


    // 1.오늘 날짜 내역 조회 API
    @GetMapping("/today")
    public TransactionGetResponse getTodayTransactions(@RequestParam Long userId) {
        LocalDateTime today = LocalDateTime.now();
        return transactionService.getTransactionsByDate(userId, today);
    }

    // 2.선택 날짜 내역 조회 API
    @GetMapping("/select-date")
    public TransactionGetResponse getTransactionsByDate(@RequestParam Long userId, @RequestParam String date) {
        // 전달된 날짜 문자열을 LocalDateTime 객체로 변환, 사용자에게 받은 date(yyyy-mm-dd)에 시간 추가해서 ISO-8601 표준형식으로
        LocalDateTime selectedDate = LocalDateTime.parse(date + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return transactionService.getTransactionsByDate(userId, selectedDate);
    }

    // 3.월 총 지출, 총 수입, 자산 조회 API
    @GetMapping("/total")
    public TransactionSummaryResponse getTransactionSummary(
            @RequestParam Long userId,
            @RequestParam int year,
            @RequestParam int month) {
        return transactionSummaryService.getTransactionSummary(userId, year, month);
    }

    // 4.월 내역 리스트 버튼 API
    @GetMapping("/list")
    public TransactionListResponse getTransactionList(
            @RequestParam Long userId,
            @RequestParam int year,
            @RequestParam int month) {
        return transactionListService.getTransactionList(userId, year, month);
    }

    // 5.키워드로 수입, 지출 내역 검색 API
    @GetMapping("/search")
    public TransactionSearchResponse searchTransactions(
            @RequestParam Long userId,
            @RequestParam String keyword) {
        return transactionSearchService.searchTransactions(userId, keyword);
    }

    // 6.수입 및 지출 내역 입력 API
    @PostMapping(value = "/post",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createTransaction(
            @RequestBody TransactionPostRequest request) {

        TransactionPostResponse response = transactionPostService.createTransaction(request);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put(response.getMessage().contains("Income") ? "income_id" : "usage_id", response.getId());
        responseMap.put("message", response.getMessage());
        return ResponseEntity.ok(responseMap);
    }

    // 7.수입 및 지출 내역 삭제 API
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteTransaction(
            @RequestParam(required = false) Long incomeId,
            @RequestParam(required = false) Long usageId) {

        if (incomeId != null) {
            TransactionDeleteResponse response = transactionDeleteService.deleteTransaction(incomeId, null);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put(response.getResponseKey(), response.getId());
            responseMap.put("message", response.getMessage());
            return ResponseEntity.ok(responseMap);
        } else if (usageId != null) {
            TransactionDeleteResponse response = transactionDeleteService.deleteTransaction(null, usageId);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put(response.getResponseKey(), response.getId());
            responseMap.put("message", response.getMessage());
            return ResponseEntity.ok(responseMap);
        } else {
            throw new IllegalArgumentException("Invalid or missing id parameter");
        }
    }

    // 8.수입 및 지출 내역 수정 API
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateTransaction(
            @RequestBody TransactionPutRequest request,
            @RequestParam(required = false) Long incomeId,
            @RequestParam(required = false) Long usageId) {

        if (incomeId != null) {
            TransactionPutResponse response = transactionPutService.updateTransaction(request, incomeId, null);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put(response.getMessage().contains("Income") ? "income_id" : "usage_id", response.getId());
            responseMap.put("message", response.getMessage());
            return ResponseEntity.ok(responseMap);
        } else if (usageId != null) {
            TransactionPutResponse response = transactionPutService.updateTransaction(request, null, usageId);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put(response.getMessage().contains("Income") ? "income_id" : "usage_id", response.getId());
            responseMap.put("message", response.getMessage());
            return ResponseEntity.ok(responseMap);
        } else {
            throw new IllegalArgumentException("Invalid or missing id parameter");
        }
    }

    // 9. 개별 상세 내역 조회
    @GetMapping("/get")
    public ResponseEntity<Map<String, Object>> getTransactionById(
            @RequestParam(required = false) Long incomeId,
            @RequestParam(required = false) Long usageId) {

        // 서비스 호출
        // userId 또는 usageId를 통해 조회
        if (incomeId != null) {
            return ResponseEntity.ok(transactionService.getIncomeById(incomeId));
        } else if (usageId != null) {
            return ResponseEntity.ok(transactionService.getUsageById(usageId));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid or missing id parameter"));
        }
    }
}