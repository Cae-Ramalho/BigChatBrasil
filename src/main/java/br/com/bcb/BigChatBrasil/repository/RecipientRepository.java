package br.com.bcb.BigChatBrasil.repository;

import br.com.bcb.BigChatBrasil.domain.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository de destinatários.
 *
 * Destinatário = usuário que recebe mensagens do cliente
 */
public interface RecipientRepository extends JpaRepository<Recipient, Long> {

    /**
     * Busca destinatário por telefone
     */
    Optional<Recipient> findByPhoneRecipient(String phoneRecipient);

    /**
     * Busca por ID
     */
    Optional<Recipient> findByIdRecipient(Long idRecipient);
}
