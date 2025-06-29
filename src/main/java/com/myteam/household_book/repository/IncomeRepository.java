package com.myteam.household_book.repository;

import com.myteam.household_book.entity.Income; // Income 엔티티에 대해 CRUD 작업 수행
import org.springframework.data.jpa.repository.JpaRepository; //JpaRepository는 CRUD 작업을 자동으로 제공
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    // 카테고리별 소득 통계 (날짜 범위)
    @Query("SELECT i.incomeCategory as categoryId, SUM(i.incomePrice) as totalAmount " +
            "FROM Income i " +
            "WHERE i.userId.userId = :userId " +
            "AND i.incomeDate BETWEEN :startDate AND :endDate " +
            "GROUP BY i.incomeCategory " +
            "ORDER BY SUM(i.incomePrice) DESC")
    List<Object[]> findIncomeStatsByCategory(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // 월별 카테고리 통계 (기존 년/월 방식 활용)
    @Query("SELECT i.incomeCategory as categoryId, SUM(i.incomePrice) as totalAmount " +
            "FROM Income i " +
            "WHERE i.userId.userId = :userId " +
            "AND FUNCTION('YEAR', i.incomeDate) = :year " +
            "AND FUNCTION('MONTH', i.incomeDate) = :month " +
            "GROUP BY i.incomeCategory " +
            "ORDER BY SUM(i.incomePrice) DESC")
    List<Object[]> findIncomeStatsByCategoryForMonth(
            @Param("userId") Long userId,
            @Param("year") int year,
            @Param("month") int month);

    // 총 소득 계산 (날짜 범위)
    @Query("SELECT SUM(i.incomePrice) FROM Income i " +
            "WHERE i.userId.userId = :userId " +
            "AND i.incomeDate BETWEEN :startDate AND :endDate")
    Long findTotalIncomeByUserAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // 월별 총 소득 (기존 방식 활용)
    @Query("SELECT SUM(i.incomePrice) FROM Income i " +
            "WHERE i.userId.userId = :userId " +
            "AND FUNCTION('YEAR', i.incomeDate) = :year " +
            "AND FUNCTION('MONTH', i.incomeDate) = :month")
    Long findTotalIncomeByUserAndMonth(
            @Param("userId") Long userId,
            @Param("year") int year,
            @Param("month") int month);
}
