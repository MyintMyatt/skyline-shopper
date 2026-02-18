package com.orion.auth_service.entity;

import com.orion.auth_service.common.constant.Provider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Table(name = "tbl_auth_provider")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // users can link their Google , Microsoft account
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Provider provider;

    @Column(nullable = false)
    private String provider_user_id;

    @CreatedDate
    private Instant createdAt;
}
