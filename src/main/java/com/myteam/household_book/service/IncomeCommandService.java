package com.myteam.household_book.service;

import com.myteam.household_book.dto.income.IncomeDtos;
import com.myteam.household_book.entity.Income;
import com.myteam.household_book.entity.User;
import com.myteam.household_book.repository.IncomeRepository;
import com.myteam.household_book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IncomeCommandService {

    private final IncomeRepository incomeRepo;
    private final UserRepository userRepo;

    @Transactional
    public IncomeDtos.Response create(IncomeDtos.Create dto) {
        User user = userRepo.findById(dto.userId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음: " + dto.userId()));

        Income i = new Income();
        i.setUserId(user);
        i.setIncomeTitle(dto.incomeTitle());
        i.setIncomeMemo(dto.incomeMemo());
        i.setIncomePrice(dto.incomePrice());
        i.setIncomeDate(dto.incomeDate());
        i.setIncomeCategory(dto.incomeCategory());
        i.setCreatedAt(LocalDateTime.now());

        Income saved = incomeRepo.save(i);
        return toResponse(saved);
    }

    @Transactional
    public IncomeDtos.Response update(Long incomeId, Long userId, IncomeDtos.Update dto) {
        Income i = incomeRepo.findByIncomeIdAndUserId_UserId(incomeId, userId)
                .orElseThrow(() -> new IllegalArgumentException("수입 항목이 없거나 소유자가 아님"));

        if (dto.incomeTitle() != null)     i.setIncomeTitle(dto.incomeTitle());
        if (dto.incomeMemo() != null)      i.setIncomeMemo(dto.incomeMemo());
        if (dto.incomePrice() != null)     i.setIncomePrice(dto.incomePrice());
        if (dto.incomeDate() != null)      i.setIncomeDate(dto.incomeDate());
        if (dto.incomeCategory() != null)  i.setIncomeCategory(dto.incomeCategory());

        return toResponse(i);
    }

    @Transactional
    public void delete(Long incomeId, Long userId) {
        Income i = incomeRepo.findByIncomeIdAndUserId_UserId(incomeId, userId)
                .orElseThrow(() -> new IllegalArgumentException("수입 항목이 없거나 소유자가 아님"));
        incomeRepo.delete(i);
    }

    private IncomeDtos.Response toResponse(Income i) {
        return new IncomeDtos.Response(
                i.getIncomeId(),
                i.getUserId().getUserId(),
                i.getIncomeTitle(),
                i.getIncomeMemo(),
                i.getIncomePrice(),
                i.getIncomeDate(),
                i.getCreatedAt(),
                i.getIncomeCategory()
        );
    }
}
