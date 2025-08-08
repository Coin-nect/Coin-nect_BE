package com.myteam.household_book.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("사용자 없음 (userId=" + userId + ")");
    }
}