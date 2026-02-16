package br.com.bcb.BigChatBrasil.domain.entity;

import br.com.bcb.BigChatBrasil.domain.enums.PriorityMessage;
import br.com.bcb.BigChatBrasil.domain.enums.StatusMessage;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMessage;

    /* ================= RELACIONAMENTOS ================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_conversation", nullable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recipient", nullable = false)
    private Recipient recipient;

    /* ================= DADOS ================= */

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contentMessage;

    private LocalDateTime dateSendMessage;

    @Enumerated(EnumType.STRING)
    private PriorityMessage priorityMessage;

    @Enumerated(EnumType.STRING)
    private StatusMessage statusMessage;

    @Column(precision = 15, scale = 4)
    private BigDecimal costMessage;

    private LocalDateTime dateDelivered;
    private LocalDateTime dateProcessed;
    private LocalDateTime dateFailed;

    /* ================= DOMAIN RULES ================= */

    @PrePersist
    public void beforeInsert() {

        if (dateSendMessage == null)
            dateSendMessage = LocalDateTime.now();

        if (priorityMessage == null)
            priorityMessage = PriorityMessage.normal;

        statusMessage = StatusMessage.queued;

        calculateCost();
    }

    private void calculateCost() {
        if (priorityMessage == PriorityMessage.urgent)
            costMessage = new BigDecimal("0.50");
        else
            costMessage = new BigDecimal("0.25");
    }

    public void markProcessing() {
        statusMessage = StatusMessage.processing;
        dateProcessed = LocalDateTime.now();
    }

    public void markDelivered() {
        statusMessage = StatusMessage.delivered;
        dateDelivered = LocalDateTime.now();
    }

    public void markFailed() {
        statusMessage = StatusMessage.failed;
        dateFailed = LocalDateTime.now();
    }
}
