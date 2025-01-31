/* 예산 그래프 API의 응답 형식(성공 여부, 메시지, 그래프 데이터 리스트 포함). */
package com.myteam.household_book.budget;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BudgetGraphResponse {
    private boolean isSuccess; // 성공 여부
    private int code; // 응답 코드
    private String message; // 응답 메시지
    private String comment; // 추가 설명 (선택적)
    private List<BudgetGraphData> graphData; // 그래프 데이터

    // 생성자: 5개월치 데이터가 모두 있는 경우
    public BudgetGraphResponse(boolean isSuccess, int code, String message, List<BudgetGraphData> graphData) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.graphData = graphData;
    }

    // 생성자: 5개월치 데이터가 일부만 있는 경우
    public BudgetGraphResponse(boolean isSuccess, int code, String message, String comment, List<BudgetGraphData> graphData) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.comment = comment;
        this.graphData = graphData;
    }
}