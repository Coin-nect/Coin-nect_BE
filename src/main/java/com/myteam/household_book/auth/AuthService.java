package com.myteam.household_book.auth;

import com.myteam.household_book.dto.UserLoginRequestDto;
import com.myteam.household_book.dto.UserSignupRequestDto;
import com.myteam.household_book.entity.User;
import com.myteam.household_book.jwt.JwtUtil;
import com.myteam.household_book.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    public void signup(UserSignupRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // 비밀번호 암호화
        user.setUserBirthDate(dto.getUserBirthDate());
        user.setGender(dto.getGender());
        user.setCreatedAt(Timestamp.from(Instant.now()));
        user.setStatus((byte) 1); // 상태 기본값

        userRepository.save(user);
    }

    public String login(UserLoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.generateToken(user.getUserId(), user.getUsername());


    }

    @Transactional
    public User upsertKakaoUser(Long kakaoId, String nickname, String email) {
        return userRepository.findByKakaoId(kakaoId).orElseGet(() -> {
            User u = new User();
            u.setKakaoId(kakaoId);
            u.setUsername(nickname);
            u.setEmail(email);
            u.setPassword("kakao-oauth"); // 필요시 랜덤/인코딩
            u.setGender(User.Gender.O);
            u.setAlarmEnabled(false);
            u.setStatus((byte)1);
            u.setCreatedAt(Timestamp.from(Instant.now()));
            u.setUserBirthDate(LocalDate.of(2000,1,1));
            return userRepository.save(u);
        });
    }

}
