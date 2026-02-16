package br.com.bcb.BigChatBrasil.dto.response;

import br.com.bcb.BigChatBrasil.domain.enums.StatusConversation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ConversationResponseDTO {

    private Long idConversation;
    private Long clientId;
    private Long recipientId;
    private String recipientName;

    private String lastMessageContent;
    private LocalDateTime lastMessageTime;
    private Long unreadCount;

    private StatusConversation statusConversation;
}
