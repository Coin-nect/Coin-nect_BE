package com.myteam.household_book.login;

import com.myteam.household_book.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth/kakao")
public class KakaoAuthController {

    private static final String CLIENT_ID = "f3583217e9748ae4e2b0a7a27e0fe440";
    private static final String REDIRECT_URI = "http://localhost:2705/auth/kakao/callback";
    // 지금은 2705포트에서 테스트 가능하도록 해둠 나중에 배포 서버에서 테스트하려면 추가 환경 수정 필요.
    @GetMapping("/login")
    public ResponseEntity<?> kakaoLogin() {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=code";

        return ResponseEntity.ok(kakaoAuthUrl);
    }

    private final KakaoAuthService kakaoAuthService;
    private final JwtUtil jwtUtil;

    @Autowired
    public KakaoAuthController(KakaoAuthService kakaoAuthService, JwtUtil jwtUtil) {
        this.kakaoAuthService = kakaoAuthService;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping("/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) {
        String accessToken = kakaoAuthService.getKakaoAccessToken(code);
        String nickname = kakaoAuthService.getKakaoUserNickname(accessToken);
        String jwt = jwtUtil.generateToken(nickname);
        return ResponseEntity.ok(jwt);
    }




}
