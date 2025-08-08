package com.myteam.household_book;

import com.myteam.household_book.dto.UserProfileDto;
import com.myteam.household_book.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserProfileTest {

    @Autowired
    private UserService userService;

    @Test
    public void testGetUserProfile() {
        Long testUserId = 1L;

        UserProfileDto profile = userService.getUserProfile(testUserId);

        if (profile == null) {
            System.out.println("DB에서 userId=" + testUserId + " 데이터가 없습니다.");
        } else {
            System.out.println("조회 성공");
            System.out.println("userId: " + profile.getUserId());
            System.out.println("username: " + profile.getUsername());
            System.out.println("email: " + profile.getEmail());
            System.out.println("birth: " + profile.getUserBirthDate());
            System.out.println("gender: " + profile.getGender());
        }
    }
}
