package com.myteam.household_book.controller;

import com.myteam.household_book.entity.User;
import com.myteam.household_book.jwt.JwtUtil;
import com.myteam.household_book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth/guest")
@RequiredArgsConstructor
public class GuestLoginController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> guestLogin() {
        // 랜덤 닉네임
        String nickname = "Guest_" + UUID.randomUUID().toString().substring(0, 8);

        // 게스트 사용자 생성/저장
        User user = new User();
        user.setUsername(nickname);
        user.setPassword("guest-login");
        user.setGender(User.Gender.O);
        user.setAlarmEnabled(false);
        user.setStatus((byte) 1);
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        user.setUserBirthDate(LocalDate.of(2000, 1, 1));

        userRepository.save(user);

        // ★ JWT 발급: userId + username 포함
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "nickname", nickname
        ));
    }
}
