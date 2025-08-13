package com.myteam.household_book.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfo {
    private Long kakaoId;
    private String nickname;
    private String email;
}
