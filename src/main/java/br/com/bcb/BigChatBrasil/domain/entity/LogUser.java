package br.com.bcb.BigChatBrasil.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import br.com.bcb.BigChatBrasil.domain.enums.UserActionType;

@Entity
@Table(name = "log_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLogUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(nullable = false)
    private UserActionType actionType;

    @Column(nullable = false, length = 255)
    private String action;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime dateAction = LocalDateTime.now();
}
