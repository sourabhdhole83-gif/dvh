package com.dvh.user.entity;

import com.dvh.common.entity.BaseEntity;
import com.dvh.user.constants.Role;
import com.dvh.user.constants.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users",
uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_email",columnNames = "email"),
        @UniqueConstraint(name = "uk_user_phone",columnNames = "phone")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 40)
    private String name;

    @Column(nullable = false,length = 40)
    private String email;

    @Column(nullable = false,length = 10)
    private String phone;

    @Column(name = "password_hash",nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus;

}

