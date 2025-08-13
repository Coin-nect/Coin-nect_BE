package com.myteam.household_book.controller;

import com.myteam.household_book.dto.ListQuery;
import com.myteam.household_book.entity.Usage;
import com.myteam.household_book.service.UsageQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usages")
@RequiredArgsConstructor
public class UsageController {

    private final UsageQueryService usageQueryService;

    @GetMapping
    public Page<Usage> list(@RequestParam Long userId,
                            @RequestParam int year,
                            @RequestParam int month,
                            @RequestParam Long categoryId,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "20") int size) {
        return usageQueryService.listByCategory(userId, new ListQuery(year, month, categoryId, page, size));
    }
}
