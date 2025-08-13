package com.myteam.household_book.login;

import com.myteam.household_book.entity.User;
import com.myteam.household_book.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
public class KakaoAuthService {

    private static final String CLIENT_ID = "f3583217e9748ae4e2b0a7a27e0fe440";
    private static final String CLIENT_SECRET = "6mbQcw0p4w0kwwKI0BfzYDIel6SG94q3";
    private static final String REDIRECT_URI = "http://localhost:2705/auth/kakao/callback";
    private static final String TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
    private final RestTemplate restTemplate;

    public KakaoAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getKakaoAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);
        params.add("client_secret", CLIENT_SECRET);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                TOKEN_REQUEST_URL,
                HttpMethod.POST,
                request,
                Map.class
        );

        return response.getBody().get("access_token").toString();
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                request,
                Map.class
        );

        return response.getBody();
    }

    public String getKakaoUserNickname(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        Map<String, Object> properties = (Map<String, Object>) body.get("properties");

        return (String) properties.get("nickname");
    }

    @Autowired
    private UserRepository userRepository;

    public User handleKakaoUser(String accessToken) {
        Map<String, Object> body = getUserInfo(accessToken);

        Long kakaoId = ((Number) body.get("id")).longValue();
        Map<String, Object> properties = (Map<String, Object>) body.get("properties");
        String nickname = properties != null ? (String) properties.get("nickname") : null;
        String profileImage = properties != null ? (String) properties.get("profile_image") : null;

        return userRepository.findByKakaoId(kakaoId).map(u -> {
            if (nickname != null && !nickname.isBlank()) u.setUsername(nickname);
            if (profileImage != null) u.setProfileImage(profileImage);
            return userRepository.save(u);
        }).orElseGet(() -> {
            User u = new User();
            u.setKakaoId(kakaoId);
            u.setUsername(nickname != null ? nickname : ("Kakao_" + kakaoId));
            u.setEmail("kakao_" + kakaoId + "@example.com");
            u.setProfileImage(profileImage);
            u.setPassword("kakao-login");
            u.setUserBirthDate(java.time.LocalDate.of(2000, 1, 1));
            u.setGender(User.Gender.O);
            u.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            u.setStatus((byte) 1);
            return userRepository.save(u);
        });
    }


    public void logoutKakaoUser(String accessToken) {
        String logoutUrl = "https://kapi.kakao.com/v1/user/logout";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.exchange(logoutUrl, HttpMethod.POST, request, String.class);
    }


}



