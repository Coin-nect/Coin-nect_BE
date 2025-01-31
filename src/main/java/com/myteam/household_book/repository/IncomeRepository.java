package com.myteam.household_book.repository;

import com.myteam.household_book.entity.Income; // Income 엔티티에 대해 CRUD 작업 수행
import org.springframework.data.jpa.repository.JpaRepository; //JpaRepository는 CRUD 작업을 자동으로 제공
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// JpaRepository를 상속받아 CRUD 작업을 자동으로 수행
public interface IncomeRepository extends JpaRepository<Income, Long> {
    // 특정 사용자(userId), 연도(year), 월(month)에 해당하는 수입 데이터 조회
    @Query("SELECT i FROM Income i WHERE i.userId.userId = :userId AND FUNCTION('YEAR', i.incomeDate) = :year AND FUNCTION('MONTH', i.incomeDate) = :month")
    List<Income> findByUserIdAndIncomeDateYearAndIncomeDateMonth(
            @Param("userId") Long userId,
            @Param("year") int year,
            @Param("month") int month
    );

    // 키워드 검색을 위한 메서드 수정
    @Query("SELECT i FROM Income i WHERE i.userId.userId = :userId AND (i.incomeTitle LIKE %:keyword% OR i.incomeMemo LIKE %:keyword%)")
    List<Income> findByUserIdAndIncomeTitleContainingOrIncomeMemoContaining(
            @Param("userId") Long userId,
            @Param("keyword") String keyword
    );
}
