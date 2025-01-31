package com.myteam.household_book.category;

import com.myteam.household_book.entity.Category;
import com.myteam.household_book.repository.CategoryRepository;
import com.myteam.household_book.repository.UserCategoryRepository;
import com.myteam.household_book.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final UserRepository userRepository;

    // 1. 카테고리 조회 처리
    public CategoryResponse getUserCategories(Long userId, String type) {
        try {
            Category.Type categoryType;
            // 대소문자 구분 없이 타입 체크
            if ("income".equalsIgnoreCase(type)) {
                categoryType = Category.Type.INCOME;
            } else if ("expense".equalsIgnoreCase(type)) {
                categoryType = Category.Type.EXPENSE;
            } else {
                throw new IllegalArgumentException("Type must be either 'income' or 'expense'");
            }

            List<Category> categories = userCategoryRepository
                    .findCategoriesByUserIdAndType(userId, categoryType);

            List<CategoryDto> categoryDtos = categories.stream()
                    .map(category -> new CategoryDto(
                            category.getCategoryId(),
                            category.getCategoryName()))
                    .collect(Collectors.toList());

            return new CategoryResponse(true, 200, "성공",
                    new CategoryResult(categoryDtos));

        } catch (IllegalArgumentException e) {
            return new CategoryResponse(false, 400, // bad request
                    "사용자Id가 존재하지 않거나, 잘못된 카테고리 타입입니다. 'income' 또는 'expense'를 입력해주세요.", null);
        } catch (Exception e) {
            return new CategoryResponse(false, 500, // server 문제
                    "카테고리 조회 중 오류가 발생했습니다: " + e.getMessage(), null);
        }
    }
}
