package com.myteam.household_book.repository;

import com.myteam.household_book.dto.CategorySumDto;
import com.myteam.household_book.entity.Usage;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
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

    // 월별 카테고리 통계
    @Query("SELECT u.usageCategory as categoryId, SUM(u.usagePrice) as totalAmount " +
            "FROM Usage u " +
            "WHERE u.userId.userId = :userId " +
            "AND FUNCTION('YEAR', u.usageDate) = :year " +
            "AND FUNCTION('MONTH', u.usageDate) = :month " +
            "GROUP BY u.usageCategory " +
            "ORDER BY SUM(u.usagePrice) DESC")
    List<Object[]> findUsageStatsByCategoryForMonth(
            @Param("userId") Long userId,
            @Param("year") int year,
            @Param("month") int month);

    // 날짜 범위 카테고리 통계
    @Query("SELECT u.usageCategory as categoryId, SUM(u.usagePrice) as totalAmount " +
            "FROM Usage u " +
            "WHERE u.userId.userId = :userId " +
            "AND u.usageDate BETWEEN :startDate AND :endDate " +
            "GROUP BY u.usageCategory " +
            "ORDER BY SUM(u.usagePrice) DESC")
    List<Object[]> findUsageStatsByCategory(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // 월별 총 지출
    @Query("SELECT SUM(u.usagePrice) FROM Usage u " +
            "WHERE u.userId.userId = :userId " +
            "AND FUNCTION('YEAR', u.usageDate) = :year " +
            "AND FUNCTION('MONTH', u.usageDate) = :month")
    Long findTotalUsageByUserAndMonth(
            @Param("userId") Long userId,
            @Param("year") int year,
            @Param("month") int month);

    // 날짜 범위 총 지출
    @Query("SELECT SUM(u.usagePrice) FROM Usage u " +
            "WHERE u.userId.userId = :userId " +
            "AND u.usageDate BETWEEN :startDate AND :endDate")
    Long findTotalUsageByUserAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // 가장 이른 사용일
    @Query("""
       SELECT MIN(u.usageDate) FROM Usage u
       WHERE u.userId.userId = :userId
       """)
    LocalDateTime findMinDateByUserId(@Param("userId") Long userId);

    // 가장 늦은 사용일
    @Query("""
       SELECT MAX(u.usageDate) FROM Usage u
       WHERE u.userId.userId = :userId
       """)
    LocalDateTime findMaxDateByUserId(@Param("userId") Long userId);

    @Query("""
select coalesce(sum(u.usagePrice),0)
from Usage u
where u.userId.userId = :userId
  and u.usageDate >= :startDate and u.usageDate < :endDate
""")
    Long totalByMonth(@Param("userId") Long userId,
                      @Param("startDate") LocalDateTime start,
                      @Param("endDate") LocalDateTime end);

    @Query("""
select new com.myteam.household_book.dto.CategorySumDto(u.usageCategory, sum(u.usagePrice))
from Usage u
where u.userId.userId = :userId
  and u.usageDate >= :startDate and u.usageDate < :endDate
group by u.usageCategory
order by sum(u.usagePrice) desc
""")
    List<CategorySumDto> sumByCategory(@Param("userId") Long userId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    // 지출 상세 리스트 (카테고리별)
    @Query("""
      select u
      from Usage u
      where u.userId.userId = :userId
        and u.usageDate >= :start and u.usageDate < :end
        and u.usageCategory = :categoryId
      """)
    Page<Usage> listByCategory(@Param("userId") Long userId,
                               @Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end,
                               @Param("categoryId") Long categoryId,
                               Pageable pageable);
}