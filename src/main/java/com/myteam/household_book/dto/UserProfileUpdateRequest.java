package com.myteam.household_book.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class UserProfileUpdateRequest {

    @NotNull
    private Long userId;

    private String username;

    private LocalDate userBirthDate;

    @Pattern(regexp = "M|F|O", message = "성별은 M, F, O 중 하나여야 합니다.")
    private String gender;

    @Email
    private String email;

    private String profileImage;

    // Getter & Setter
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDate getUserBirthDate() { return userBirthDate; }
    public void setUserBirthDate(LocalDate userBirthDate) { this.userBirthDate = userBirthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}
