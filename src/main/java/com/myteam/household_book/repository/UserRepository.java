// User 엔티티를 데이터베이스에서 조회하거나 수정하는 역할 JPA

package com.myteam.household_book.repository;

import com.myteam.household_book.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}