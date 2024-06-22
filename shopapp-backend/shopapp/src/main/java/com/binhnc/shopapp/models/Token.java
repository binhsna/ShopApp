package com.binhnc.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token // Token: Chữ ký số
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token", length = 255, nullable = false)
    private String token;

    @Column(name = "token_type", length = 50)
    private String tokenType;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    private boolean revoked; // Đã thu hồi
    private boolean expired; // Đã hết hạn

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_mobile")
    private boolean isMobile;
}
