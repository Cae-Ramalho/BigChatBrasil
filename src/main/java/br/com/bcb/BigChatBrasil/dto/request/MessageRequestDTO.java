package br.com.bcb.BigChatBrasil.dto.request;

import br.com.bcb.BigChatBrasil.domain.enums.PriorityMessage;
import lombok.Data;

/**
 * Request para envio de mensagem
 * clientId identifica o cliente remetente
 */
@Data
public class MessageRequestDTO {

    private Long clientId;
    private Long recipientId;
    private Long conversationId; // opcional (pode ser nulo para nova conversa)
    private String content;
    private PriorityMessage priority; // normal ou urgent
}