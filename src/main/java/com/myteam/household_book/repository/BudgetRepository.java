// 예산 정보 조회 JPA
package com.myteam.household_book.repository;

import com.myteam.household_book.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("SELECT b FROM Budget b WHERE b.userId.userId = :userId AND b.startDate <= :date AND b.endDate >= :date")
    Optional<Budget> findByUserIdAndDate(
            @Param("userId") Long userId,
            @Param("date") LocalDate date
    );
}