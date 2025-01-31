//CategoryDto 객체들의 리스트를 포함
//CategoryDto 리스트를 categories 필드에 포함하여, 카테고리 목록을 클라이언트에게 전달할 수 있는 형식을 제공

//CategoryDto: 각 카테고리의 정보를 담고,
//CategoryResult: 여러 개의 CategoryDto를 리스트로 담고,
//CategoryResponse: 응답 상태와 결과 데이터를 포함하여 API 응답으로 반환

package com.myteam.household_book.category;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryResult {
    private List<CategoryDto> categories;

    public CategoryResult(List<CategoryDto> categories) {
        this.categories = categories;
    }
}