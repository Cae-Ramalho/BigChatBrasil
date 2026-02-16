package br.com.bcb.BigChatBrasil.dto.request;

import br.com.bcb.BigChatBrasil.domain.enums.PriorityMessage;
import lombok.Data;

/**
 * DTO de envio de mensagem
 */
@Data
public class SendMessageRequestDTO {

    private Long recipientId;
    private String content;
    private PriorityMessage priority; // normal ou urgent
}
