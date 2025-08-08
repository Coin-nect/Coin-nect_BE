package com.myteam.household_book.service;

import com.myteam.household_book.dto.UserProfileDto;
import com.myteam.household_book.entity.User;
import com.myteam.household_book.exception.UserNotFoundException;
import com.myteam.household_book.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return new UserProfileDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileImage(),
                user.getUserBirthDate(),
                user.getGender().name()
        );
    }

}

