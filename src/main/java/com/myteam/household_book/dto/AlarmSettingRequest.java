package com.myteam.household_book.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmSettingRequest {
    private Long userId;
    private boolean alarmEnabled;
}
