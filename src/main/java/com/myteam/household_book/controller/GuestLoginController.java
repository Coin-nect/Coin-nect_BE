package com.myteam.household_book.controller;

import com.myteam.household_book.entity.User;
import com.myteam.household_book.jwt.JwtUtil;
import com.myteam.household_book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth/guest")
@RequiredArgsConstructor
public class GuestLoginController {

    private final
    UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> guestLogin() {
        // 랜덤 닉네임 생성 (예: Guest_8391)
        String nickname = "Guest_" + UUID.randomUUID().toString().substring(0, 8);

        // DB에 사용자 저장 (이메일, 비밀번호, 생년월일 등 없이)
        User user = new User();
        user.setUsername(nickname);
        user.setPassword("guest-login");
        user.setGender(User.Gender.O);
        user.setAlarmEnabled(false);
        user.setStatus((byte) 1);
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        user.setUserBirthDate(LocalDate.of(2000, 1, 1));


        userRepository.save(user);

        // JWT 발급 (닉네임 기준)
        String token = jwtUtil.generateToken(nickname);

        return ResponseEntity.ok(Map.of("token", token, "nickname", nickname));
    }
}
