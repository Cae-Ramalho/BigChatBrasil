package br.com.bcb.BigChatBrasil.repository;

import br.com.bcb.BigChatBrasil.domain.entity.Client;
import br.com.bcb.BigChatBrasil.domain.entity.Conversation;
import br.com.bcb.BigChatBrasil.domain.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    /**
     * Busca conversa específica entre cliente e destinatário
     */
    Optional<Conversation> findByClientAndRecipient(Client client, Recipient recipient);

    /**
     * Lista todas as conversas de um cliente
     */
    List<Conversation> findByClient(Client client);
}
