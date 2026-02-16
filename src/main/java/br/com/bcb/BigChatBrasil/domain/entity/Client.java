package br.com.bcb.BigChatBrasil.domain.entity;

import br.com.bcb.BigChatBrasil.domain.enums.DocumentType;
import br.com.bcb.BigChatBrasil.domain.enums.PlanType;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClient;

    @Column(nullable = false, length = 150)
    private String nameClient;

    @Column(nullable = false, unique = true, length = 15)
    private String documentClient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentTypeClient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanType planTypeClient;

    @Column(precision = 15, scale = 4)
    private BigDecimal balanceClient;

    @Column(precision = 15, scale = 4)
    private BigDecimal limitClient;

    @Column(nullable = false)
    private Boolean boActiveClient;

    @Column(precision = 15, scale = 4)
    private BigDecimal monthlyUsage;

    private LocalDateTime lastReset;

    private LocalDateTime dtCreate;

    /* =========================================================
       LIFECYCLE (SOMENTE DEFAULTS TÉCNICOS — NÃO REGRAS)
       ========================================================= */

    @PrePersist
    public void beforeInsert() {

        if (dtCreate == null)
            dtCreate = LocalDateTime.now();

        if (boActiveClient == null)
            boActiveClient = true;

        if (balanceClient == null)
            balanceClient = BigDecimal.ZERO;

        if (limitClient == null)
            limitClient = BigDecimal.ZERO;

        if (monthlyUsage == null)
            monthlyUsage = BigDecimal.ZERO;
    }

    /* =========================================================
       DOMAIN RULES
       ========================================================= */

    public void validateCanSendMessage() {
        if (!boActiveClient)
            throw new IllegalStateException("Mensagem não Enviada! Cliente Inativo.");
    }

    /**
     * Crédito disponível para envio de mensagens
     */
    public BigDecimal getAvailableCredit() {

        if (planTypeClient == PlanType.prepaid) {
            return balanceClient;
        }

        // postpaid
        return limitClient.subtract(monthlyUsage);
    }

    /**
     * Método principal do domínio:
     * cobra o envio de uma mensagem
     */
    public void charge(BigDecimal cost) {

        validateCanSendMessage();

        if (cost == null || cost.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Invalid message cost");

        if (planTypeClient == PlanType.prepaid) {
            chargePrepaid(cost);
            return;
        }

        chargePostpaid(cost);
    }

    private void chargePrepaid(BigDecimal cost) {

        if (balanceClient.compareTo(cost) < 0)
            throw new IllegalStateException("Insufficient balance");

        balanceClient = balanceClient.subtract(cost);
    }

    private void chargePostpaid(BigDecimal cost) {

        BigDecimal available = limitClient.subtract(monthlyUsage);

        if (available.compareTo(cost) < 0)
            throw new IllegalStateException("Credit limit exceeded");

        monthlyUsage = monthlyUsage.add(cost);
    }

    /**
     * Reset mensal do plano pós-pago
     */
    public void resetMonthlyUsage() {
        this.monthlyUsage = BigDecimal.ZERO;
        this.lastReset = LocalDateTime.now();
    }

    /**
     * Desativa cliente
     */
    public void deactivate() {
        this.boActiveClient = false;
    }

    /**
     * Reativa cliente
     */
    public void activate() {
        this.boActiveClient = true;
    }
}
