package com.myteam.household_book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@Table(name = "usages")
@Getter
@Setter
public class Usage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    private Long usageId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @Column(name = "usage_title", nullable = false)
    private String usageTitle;

    @Column(name = "usage_memo")
    private String usageMemo;

    @Column(name = "usage_price", nullable = false)
    private Integer usagePrice;

    @Column(name = "usage_date", nullable = false)
    private LocalDateTime usageDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "usage_category", nullable = false)
    private Integer usageCategory;
}
