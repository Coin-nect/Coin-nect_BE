// Category 엔티티를 데이터베이스에서 조회하거나 수정 JPA

package com.myteam.household_book.repository;

import com.myteam.household_book.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// 카테고리 조회 (특정범위 내의 카테고리 조회)
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByCategoryIdBetween(Long start, Long end);
}