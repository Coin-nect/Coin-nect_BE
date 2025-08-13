package com.myteam.household_book.controller;

import com.myteam.household_book.dto.income.IncomeDtos;
import com.myteam.household_book.entity.Income;
import com.myteam.household_book.service.IncomeCommandService;
import com.myteam.household_book.service.IncomeQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeCommandService commandService;
    private final IncomeQueryService queryService;

    // 리스트(카테고리 필터 + 페이징)
    @GetMapping
    public Page<Income> list(@RequestParam Long userId,
                             @RequestParam int year,
                             @RequestParam int month,
                             @RequestParam(required = false) Long categoryId,
                             Pageable pageable) {
        return queryService.listByCategory(userId, year, month, categoryId, pageable);
    }

    // 등록
    @PostMapping
    public ResponseEntity<IncomeDtos.Response> create(@Valid @RequestBody IncomeDtos.Create dto) {
        return ResponseEntity.ok(commandService.create(dto));
    }

    // 수정 (소유자 검증을 위해 userId는 쿼리로 받음)
    @PatchMapping("/{incomeId}")
    public ResponseEntity<IncomeDtos.Response> update(@PathVariable Long incomeId,
                                                      @RequestParam Long userId,
                                                      @Valid @RequestBody IncomeDtos.Update dto) {
        return ResponseEntity.ok(commandService.update(incomeId, userId, dto));
    }

    // 삭제 (소유자 검증용 userId)
    @DeleteMapping("/{incomeId}")
    public ResponseEntity<Void> delete(@PathVariable Long incomeId,
                                       @RequestParam Long userId) {
        commandService.delete(incomeId, userId);
        return ResponseEntity.noContent().build();
    }
}
