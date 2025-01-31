// 카테고리 조회 결과를 포함하는 응답을 반환

//CategoryDto: 각 카테고리의 정보를 담고,
//CategoryResult: 여러 개의 CategoryDto를 리스트로 담고,
//CategoryResponse: 응답 상태와 결과 데이터를 포함하여 API 응답으로 반환
package com.myteam.household_book.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
    private boolean isSuccess;
    private int code;
    private String message;
    private CategoryResult result;

    public CategoryResponse(boolean isSuccess, int code, String message, CategoryResult result) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
