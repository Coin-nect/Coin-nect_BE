package com.myteam.household_book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(name = "user_birth_date", nullable = false)
    private LocalDate userBirthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "created_at")
    private Timestamp createdAt;

    private Byte status;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "profile_image", columnDefinition = "TEXT")
    private String profileImage;

    public enum Gender {
        M, F, O
    }

    @OneToMany(mappedBy = "user")
    private List<UserCategory> userCategories;

}