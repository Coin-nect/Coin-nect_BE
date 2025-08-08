package com.myteam.household_book.dto;

import java.time.LocalDate;

public class UserProfileDto {
    private Long userId;
    private String username;
    private String email;
    private String profileImage;
    private LocalDate userBirthDate;
    private String gender;

    public UserProfileDto(Long userId, String username, String email,
                          String profileImage, LocalDate userBirthDate, String gender) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
        this.userBirthDate = userBirthDate;
        this.gender = gender;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public LocalDate getUserBirthDate() { return userBirthDate; }
    public void setUserBirthDate(LocalDate userBirthDate) { this.userBirthDate = userBirthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}
