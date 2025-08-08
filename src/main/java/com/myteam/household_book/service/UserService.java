package com.myteam.household_book.service;

import com.myteam.household_book.dto.UserProfileDto;
import com.myteam.household_book.dto.UserProfileUpdateRequest;
import com.myteam.household_book.entity.User;
import com.myteam.household_book.exception.UserNotFoundException;
import com.myteam.household_book.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return toDto(user);
    }

    @Transactional
    public UserProfileDto updateUserProfile(UserProfileUpdateRequest req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new UserNotFoundException(req.getUserId()));

        if (req.getUsername() != null) user.setUsername(req.getUsername());
        if (req.getUserBirthDate() != null) user.setUserBirthDate(req.getUserBirthDate());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        if (req.getProfileImage() != null) user.setProfileImage(req.getProfileImage());
        if (req.getGender() != null) {
            user.setGender(User.Gender.valueOf(req.getGender()));  // "M", "F", "O"
        }

        return toDto(user);
    }

    private UserProfileDto toDto(User user) {
        return new UserProfileDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileImage(),
                user.getUserBirthDate(),
                user.getGender() != null ? user.getGender().name() : null
        );
    }
}
