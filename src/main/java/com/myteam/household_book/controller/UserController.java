package com.myteam.household_book.controller;

import com.myteam.household_book.dto.UserProfileDto;
import com.myteam.household_book.dto.UserProfileUpdateRequest;
import com.myteam.household_book.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

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

}
