package com.myteam.household_book.controller;

import com.myteam.household_book.dto.AlarmSettingRequest;
import com.myteam.household_book.dto.UserProfileDto;
import com.myteam.household_book.dto.UserProfileUpdateRequest;
import com.myteam.household_book.entity.User;
import com.myteam.household_book.jwt.JwtUtil;
import com.myteam.household_book.repository.UserRepository;
import com.myteam.household_book.security.CustomUserPrincipal;
import com.myteam.household_book.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(@RequestParam Long userId) {
        log.info(">>> /user/profile called, userId={}", userId);
        UserProfileDto dto = userService.getUserProfile(userId);
        log.info(">>> /user/profile OK username={}", dto.getUsername());
        return ResponseEntity.ok(dto);
    }

    @PostConstruct
    public void init() { System.out.println(">>> UserController LOADED"); }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateProfile(@Valid @RequestBody UserProfileUpdateRequest req) {
        log.info(">>> PUT /user/profile called, userId={}", req.getUserId());
        UserProfileDto updated = userService.updateUserProfile(req);
        log.info(">>> profile updated: username={}", updated.getUsername());
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/alarm")
    public ResponseEntity<Map<String, String>> updateAlarmSetting(@RequestBody AlarmSettingRequest request) {
        userService.updateAlarmSetting(request.getUserId(), request.isAlarmEnabled());
        return ResponseEntity.ok(Map.of("message", "알림 설정이 성공적으로 변경되었습니다."));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal CustomUserPrincipal me) {
        return ResponseEntity.ok(Map.of(
                "userId", me.getId(),
                "username", me.getUsername()
        ));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal CustomUserPrincipal me) {
        userRepository.deleteById(me.getId());
        return ResponseEntity.noContent().build();
    }

}
