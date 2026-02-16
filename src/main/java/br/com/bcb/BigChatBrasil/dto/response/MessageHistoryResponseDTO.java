package br.com.bcb.BigChatBrasil.dto.response;

import br.com.bcb.BigChatBrasil.domain.enums.PriorityMessage;
import br.com.bcb.BigChatBrasil.domain.enums.StatusMessage;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MessageHistoryResponseDTO {

    private Long idMessage;
    private String content;
    private PriorityMessage priority;
    private StatusMessage status;
    private BigDecimal cost;
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
}
