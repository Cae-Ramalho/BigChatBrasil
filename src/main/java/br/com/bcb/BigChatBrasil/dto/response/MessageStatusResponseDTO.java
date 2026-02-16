package br.com.bcb.BigChatBrasil.dto.response;

import br.com.bcb.BigChatBrasil.domain.enums.StatusMessage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO para consultar status da mensagem
 */
@Data
@Builder
public class MessageStatusResponseDTO {

    private Long messageId;
    private StatusMessage status;
    private LocalDateTime processedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime failedAt;
}
