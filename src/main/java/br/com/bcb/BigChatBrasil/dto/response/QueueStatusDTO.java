package br.com.bcb.BigChatBrasil.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * Estatísticas da fila em memória
 */
@Data
@Builder
public class QueueStatusDTO {

    private int normalQueueSize;
    private int urgentQueueSize;
    private long processedMessages;
    private long failedMessages;
}
