package com.myteam.household_book.dto;

public record ListQuery(int year, int month, Long categoryId, int page, int size) {}
