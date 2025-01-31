package com.myteam.household_book.repository;

import com.myteam.household_book.entity.Usage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// JpaRepository를 상속받아 CRUD 작업을 자동으로 수행
public interface UsageRepository extends JpaRepository<Usage, Long> {
    // 특정 사용자(userId), 연도(year), 월(month)에 해당하는 지출 데이터 조회
    @Query("SELECT u FROM Usage u WHERE u.userId.userId = :userId AND FUNCTION('YEAR', u.usageDate) = :year AND FUNCTION('MONTH', u.usageDate) = :month")
    List<Usage> findByUserIdAndUsageDateYearAndUsageDateMonth(
            @Param("userId") Long userId,
            @Param("year") int year,
            @Param("month") int month
    );

    // 키워드 검색
    @Query("SELECT u FROM Usage u WHERE u.userId.userId = :userId AND (u.usageTitle LIKE %:keyword% OR u.usageMemo LIKE %:keyword%)")
    List<Usage> findByUserIdAndUsageTitleContainingOrUsageMemoContaining(
            @Param("userId") Long userId,
            @Param("keyword") String keyword
    );
}