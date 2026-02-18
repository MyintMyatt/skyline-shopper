package com.orion.auth_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Table(name = "tbl_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Lob
    private String refreshToken;

    private Instant expiredAt;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isRevoked = false; //to kill session manually.

    @CreatedDate
    private Instant createdAt;

}
