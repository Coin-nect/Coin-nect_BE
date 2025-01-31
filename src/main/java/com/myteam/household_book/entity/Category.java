package com.myteam.household_book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    // 기본 생성자
    public Category() {}

    // 매개변수를 받는 생성자 추가
    public Category(String categoryName, Type type) {
        this.categoryName = categoryName;
        this.type = type;
    }

    public enum Type {
        EXPENSE,
        INCOME
    }

    @OneToMany(mappedBy = "category")
    private List<UserCategory> userCategories;
}