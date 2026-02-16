package br.com.bcb.BigChatBrasil.domain.entity;

import br.com.bcb.BigChatBrasil.domain.enums.StatusConversation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConversation;

    /* ================= RELACIONAMENTOS ================= */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_client")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_recipient")
    private Recipient recipient;

    /* ================= DADOS DA CONVERSA ================= */

    @Column(columnDefinition = "TEXT")
    private String lastMessageContent;

    private LocalDateTime lastMessageTime;

    @Column(nullable = false)
    private Long unreadCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConversation statusConversation;

    private LocalDateTime dtCreate;

    /* ================= LIFECYCLE ================= */

    @PrePersist
    public void beforeInsert() {

        if (unreadCount == null)
            unreadCount = 0L;

        if (statusConversation == null)
            statusConversation = StatusConversation.open;

        if (dtCreate == null)
            dtCreate = LocalDateTime.now();
    }

    /* ================= DOMAIN BEHAVIOR ================= */

    public void registerNewMessage(String content) {
        this.lastMessageContent = content;
        this.lastMessageTime = LocalDateTime.now();
        this.unreadCount++;
    }

    public void markAsRead() {
        this.unreadCount = 0L;
    }

    public void closeConversation() {
        this.statusConversation = StatusConversation.closed;
    }

    public void reopenConversation() {
        this.statusConversation = StatusConversation.open;
    }
}