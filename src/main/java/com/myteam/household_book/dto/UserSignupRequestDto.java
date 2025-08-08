package com.myteam.household_book.dto;

import com.myteam.household_book.entity.User;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserSignupRequestDto {
    private String username;
    private String email;
    private String password;
    private LocalDate userBirthDate;
    private User.Gender gender;
}