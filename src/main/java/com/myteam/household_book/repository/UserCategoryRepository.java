// UserCategory와 Category를 연결하는 테이블을 쿼리하는 JPA

package com.myteam.household_book.repository;

import com.myteam.household_book.entity.UserCategory;
import com.myteam.household_book.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
    @Query("SELECT uc.category FROM UserCategory uc " +
            "JOIN uc.category c " +
            "WHERE uc.user.userId = :userId " +
            "AND c.type = :type")
    List<Category> findCategoriesByUserIdAndType(@Param("userId") Long userId,
                                                 @Param("type") Category.Type type);
}