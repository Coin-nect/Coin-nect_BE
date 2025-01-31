package com.myteam.household_book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "incomes")
@Getter
@Setter
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키(id)가 자동으로 증가
    @Column(name = "income_id")
    private Long incomeId; // 기본 키는 Long 타입

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @Column(name = "income_title", nullable = false)
    private String incomeTitle;

    @Column(name = "income_memo")
    private String incomeMemo;

    @Column(name = "income_price", nullable = false)
    private Integer incomePrice;

    @Column(name = "income_date", nullable = false)
    private LocalDateTime incomeDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "income_category", nullable = false)
    private Integer incomeCategory;
}
