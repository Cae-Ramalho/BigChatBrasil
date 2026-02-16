package br.com.bcb.BigChatBrasil.dto.response;

import br.com.bcb.BigChatBrasil.domain.enums.StatusMessage;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Retorno após enfileirar mensagem
 */
@Data
@Builder
public class SendMessageResponseDTO {

    private Long id;
    private StatusMessage status;
    private LocalDateTime estimatedDelivery;
    private BigDecimal cost;
}
