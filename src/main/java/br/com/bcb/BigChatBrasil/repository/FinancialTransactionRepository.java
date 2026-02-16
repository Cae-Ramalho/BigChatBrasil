package br.com.bcb.BigChatBrasil.repository;

import br.com.bcb.BigChatBrasil.domain.entity.Client;
import br.com.bcb.BigChatBrasil.domain.entity.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository de movimentações financeiras.
 *
 * Responsável por registrar:
 * - Cobrança de mensagens
 * - Histórico financeiro por cliente
 */
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {

    /**
     * Lista transações de um cliente (extrato)
     */
    List<FinancialTransaction> findByClientOrderByDateTransactionDesc(Client client);
}
