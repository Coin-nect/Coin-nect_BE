// 조회된 카테고리 데이터를 클라이언트에게 전달하기 위한 DTO

//CategoryDto: 각 카테고리의 정보를 담고,
//CategoryResult: 여러 개의 CategoryDto를 리스트로 담고,
//CategoryResponse: 응답 상태와 결과 데이터를 포함하여 API 응답으로 반환

package com.myteam.household_book.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    private Long id;
    private String name;

    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
