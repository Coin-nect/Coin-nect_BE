package com.myteam.household_book.login;

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

    private static final String CLIENT_ID = "//노션참고";
    private static final String REDIRECT_URI = "http://localhost:8080/auth/kakao/callback";

    @GetMapping("/login")
    public ResponseEntity<?> kakaoLogin() {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=code";

        return ResponseEntity.ok(kakaoAuthUrl);
    }

    private final KakaoAuthService kakaoAuthService;

    @Autowired
    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) {
        // 1. 카카오에서 Access Token 받기
        String accessToken = kakaoAuthService.getKakaoAccessToken(code);

        // 2. Access Token을 이용해 사용자 정보 가져오기
        Map<String, Object> userInfo = kakaoAuthService.getUserInfo(accessToken);

        return ResponseEntity.ok(userInfo);
    }
}
