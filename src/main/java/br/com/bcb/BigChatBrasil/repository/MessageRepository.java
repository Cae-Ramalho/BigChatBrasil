package br.com.bcb.BigChatBrasil.repository;

import br.com.bcb.BigChatBrasil.domain.entity.Conversation;
import br.com.bcb.BigChatBrasil.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Lista mensagens de uma conversa ordenadas por envio
     */
    List<Message> findByConversationOrderByDateSendMessageAsc(Conversation conversation);
}
