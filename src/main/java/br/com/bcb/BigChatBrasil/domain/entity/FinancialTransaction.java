package br.com.bcb.BigChatBrasil.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_financial_transaction")
    private Long idFinancialTransaction;

    /*
     * Cliente dono do saldo
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    /*
     * Mensagem que gerou a cobrança
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_message", nullable = false)
    private Message message;

    /*
     * Conversa relacionada (importante para extrato por conversa)
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_conversation", nullable = false)
    private Conversation conversation;

    /*
     * Data da movimentação
     */
    @Column(name = "date_transaction", nullable = false)
    @Builder.Default
    private LocalDateTime dateTransaction = LocalDateTime.now();

    /*
     * Saldo antes da cobrança
     */
    @Column(name = "balance_before", nullable = false, precision = 15, scale = 4)
    @Builder.Default
    private BigDecimal balanceBefore = BigDecimal.ZERO;

    /*
     * Saldo após a cobrança
     */
    @Column(name = "balance_after", nullable = false, precision = 15, scale = 4)
    @Builder.Default
    private BigDecimal balanceAfter = BigDecimal.ZERO;
}