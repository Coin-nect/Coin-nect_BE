package com.myteam.household_book.repository;

import com.myteam.household_book.entity.Income;
import com.myteam.household_book.entity.Usage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Usage, Long> {
    // 사용자 ID와 날짜로 트랜잭션을 조회하는 메서드
    //지출
    @Query("SELECT u FROM Usage u WHERE u.userId.userId = :userId AND DATE(u.usageDate) = DATE(:date)")
    List<Usage> findUsagesByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDateTime date);
    //수입
    @Query("SELECT i FROM Income i WHERE i.userId.userId = :userId AND DATE(i.incomeDate) = DATE(:date)")
    List<Income> findIncomesByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDateTime date);
}