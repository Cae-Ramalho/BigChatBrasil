package br.com.bcb.BigChatBrasil.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRecipient;

    @Column(nullable = false, length = 150)
    private String nameRecipient;

    @Column(nullable = false, length = 20, unique = true)
    private String phoneRecipient;

    @Column(nullable = false)
    private Boolean boActiveRecipient;

    private LocalDateTime dateCreate;

    @Column(nullable = false)
    private Boolean available;

    /* ================= RELACIONAMENTO ================= */

    @JsonIgnore
    @OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Conversation> conversations = new ArrayList<>();

    /* ================= LIFECYCLE ================= */

    @PrePersist
    public void beforeInsert() {

        if (boActiveRecipient == null)
            boActiveRecipient = true;

        if (available == null)
            available = true;

        if (dateCreate == null)
            dateCreate = LocalDateTime.now();
    }

    /* ================= DOMAIN BEHAVIOR ================= */

    public boolean canReceiveMessage() {
        return boActiveRecipient && available;
    }

    public long getOpenConversations() {
        return conversations.stream()
                .filter(c -> c.getStatusConversation().name().equals("open"))
                .count();
    }

    public void setUnavailable() {
        this.available = false;
    }

    public void setAvailable() {
        this.available = true;
    }
}