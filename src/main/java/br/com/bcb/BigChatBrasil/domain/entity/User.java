package br.com.bcb.BigChatBrasil.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import br.com.bcb.BigChatBrasil.domain.enums.UserAccessType;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(nullable = false, length = 150)
    private String nameUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserAccessType userType;

    @Column(nullable = false, unique = true, length = 150)
    private String emailUser;

    @Column(nullable = false, length = 60)
    private String passwordHash;

    @Column(nullable = false)
    private Boolean boActiveUser;

    @Column(nullable = false)
    private LocalDateTime dateCreate;

    @PrePersist
    public void beforeInsert() {
        if (boActiveUser == null)
            boActiveUser = true;

        if (dateCreate == null)
            dateCreate = LocalDateTime.now();
    }
}
