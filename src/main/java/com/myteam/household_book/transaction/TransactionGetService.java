// 오늘, 특정날짜 지출 및 수입 내역 조회
package com.myteam.household_book.transaction;

import com.myteam.household_book.entity.Income;
import com.myteam.household_book.entity.Usage;
import com.myteam.household_book.repository.IncomeRepository;
import com.myteam.household_book.repository.TransactionRepository;
import com.myteam.household_book.repository.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionGetService {

    // 1. 특정 날의 지출 및 수입 내역 리스트 형식으로 조회 (userid, 날짜 파리미터로)
    @Autowired
    private TransactionRepository transactionRepository;

    // 사용자 ID와 날짜를 받아 해당 날짜의 거래 내역을 반환하는 메소드
    public TransactionGetResponse getTransactionsByDate(Long userId, LocalDateTime date) {
        // 사용자가 지정한 날짜에 해당하는 사용 내역을 데이터베이스에서 조회
        List<Usage> usages = transactionRepository.findUsagesByUserIdAndDate(userId, date);

        // 사용자가 지정한 날짜에 해당하는 수입 내역을 데이터베이스에서 조회
        List<Income> incomes = transactionRepository.findIncomesByUserIdAndDate(userId, date);

        // 만약 사용 내역과 수입 내역이 모두 비어있으면, 기본값으로 응답 반환
        if (usages.isEmpty() && incomes.isEmpty()) {
            return new TransactionGetResponse(true, 1000, "성공",
                    new TransactionGetResponse.Result(date.format(DateTimeFormatter.ISO_LOCAL_DATE), 0, 0, null, "데이터가 없습니다"));
        }

        // 수입 목록에서 모든 수입의 합계를 구하기
        int totalIncome = incomes.stream().mapToInt(Income::getIncomePrice).sum();

        // 사용 목록에서 모든 사용의 합계를 구하기
        int totalExpense = usages.stream().mapToInt(Usage::getUsagePrice).sum();

        // 수입 내역을 `TransactionCheckResponse.Result.Transaction` 객체로 변환하여 리스트로 저장
        List<TransactionGetResponse.Result.Transaction> transactions = incomes.stream()
                .map(income -> new TransactionGetResponse.Result.Transaction(
                        income.getIncomePrice(), // 수입 금액
                        income.getIncomeDate().toString(), // 수입 날짜
                        income.getIncomeCategory(), // 수입 카테고리
                        income.getIncomeTitle(), // 수입 제목
                        income.getIncomeMemo(), // 수입 메모
                        "income")) // 수입 타입
                .collect(Collectors.toList());

        // 사용 내역을 `TransactionCheckResponse.Result.Transaction` 객체로 변환하여 리스트로 저장
        transactions.addAll(usages.stream()
                .map(usage -> new TransactionGetResponse.Result.Transaction(
                        usage.getUsagePrice(), // 사용 금액
                        usage.getUsageDate().toString(), // 사용 날짜
                        usage.getUsageCategory(), // 사용 카테고리
                        usage.getUsageTitle(), // 사용 제목
                        usage.getUsageMemo(), // 사용 메모
                        "usage")) // 지출 타입
                .collect(Collectors.toList()));

        // 거래 내역이 없다면 "데이터가 없습니다"로 반환
        if (transactions.isEmpty()) {
            return new TransactionGetResponse(true, 1000, "성공",
                    new TransactionGetResponse.Result(date.format(DateTimeFormatter.ISO_LOCAL_DATE), totalIncome, totalExpense, null, "데이터가 없습니다"));
        }

        // 거래 내역을 시간 순으로 정렬
        transactions.sort((t1, t2) -> {
            LocalDateTime date1 = LocalDateTime.parse(t1.getDate());
            LocalDateTime date2 = LocalDateTime.parse(t2.getDate());
            return date1.compareTo(date2); // 시간 순으로 정렬
        });

        // 거래 내역이 있으면 리스트를 반환
        return new TransactionGetResponse(true, 1000, "성공",
                new TransactionGetResponse.Result(date.format(DateTimeFormatter.ISO_LOCAL_DATE), totalIncome, totalExpense, transactions, null));
    }


    // 2. 개별 상세 내역 조회 (수입/지출 ID가 파라미터로)
    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UsageRepository usageRepository;

    // 수입 ID로 조회
    public Map<String, Object> getIncomeById(Long incomeId) {
        Income income = incomeRepository.findById(incomeId).orElse(null);
        if (income == null) {
            return Map.of("message", "Income not found");
        }
        // 응답 형식으로 변환
        return Map.of("code", 1000, "message", "성공", "result", convertIncomeToResponse(income));
    }

    // 지출 ID로 조회
    public Map<String, Object> getUsageById(Long usageId) {
        Usage usage = usageRepository.findById(usageId).orElse(null);
        if (usage == null) {
            return Map.of("message", "Usage not found");
        }
        // 응답 형식으로 변환
        return Map.of("code", 1000, "message", "성공", "result", convertUsageToResponse(usage));
    }

    // 수입 응답 변환
    private Map<String, Object> convertIncomeToResponse(Income income) {
        return Map.of(
                "amount", income.getIncomePrice(),
                "date", income.getIncomeDate().toString(),
                "category", income.getIncomeCategory(),
                "title", income.getIncomeTitle(),
                "memo", income.getIncomeMemo(),
                "type", "income"
        );
    }

    // 지출 응답 변환
    private Map<String, Object> convertUsageToResponse(Usage usage) {
        return Map.of(
                "amount", usage.getUsagePrice(),
                "date", usage.getUsageDate().toString(),
                "category", usage.getUsageCategory(),
                "title", usage.getUsageTitle(),
                "memo", usage.getUsageMemo(),
                "type", "usage"
        );
    }
}
