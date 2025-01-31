package com.myteam.household_book.transaction;

import com.myteam.household_book.entity.Usage;
import com.myteam.household_book.entity.User;
import com.myteam.household_book.entity.Income;
import com.myteam.household_book.repository.UserRepository;
import com.myteam.household_book.repository.UsageRepository;
import com.myteam.household_book.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionPostService {

    @Autowired
    private UserRepository userRepository; //사용자 정보 관리

    @Autowired
    private UsageRepository usageRepository; //지출 내역 관리

    @Autowired
    private IncomeRepository incomeRepository; //수입 내역 관리


    // 트랜잭션 생성 메소드
    public TransactionPostResponse createTransaction(TransactionPostRequest request) {
        // 사용자 ID로 사용자를 조회하고, 없으면 예외 처리
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // 트랜잭션 타입이 'expense'인 경우 (지출 처리)
        if ("expense".equals(request.getType())) {
            Usage usage = new Usage();
            usage.setUserId(user);
            usage.setUsageTitle(request.getContent());
            usage.setUsageMemo(request.getMemo());
            usage.setUsagePrice(request.getPrice());
            usage.setUsageDate(request.getDate());
            usage.setUsageCategory(request.getCategoryId());  // 카테고리 ID 설정 추가

            usage = usageRepository.save(usage); //지출 정보 저장
            return TransactionPostResponse.fromUsage(usage.getUsageId());
            // 트랜잭션 타입이 'income'인 경우 (수입 처리)
        } else if ("income".equals(request.getType())) {
            Income income = new Income();
            income.setUserId(user);
            income.setIncomeTitle(request.getContent());
            income.setIncomeMemo(request.getMemo());
            income.setIncomePrice(request.getPrice());
            income.setIncomeDate(request.getDate());
            income.setIncomeCategory(request.getCategoryId());  // 카테고리 ID 설정 추가

            income = incomeRepository.save(income);
            return TransactionPostResponse.fromIncome(income.getIncomeId());

            // 타입이 'expense'나 'income'이 아닌 경우 예외 처리
        } else {
            throw new IllegalArgumentException("Invalid type: " + request.getType());
        }
    }
}
