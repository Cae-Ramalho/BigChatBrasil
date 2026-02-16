package br.com.bcb.BigChatBrasil.queue;

import br.com.bcb.BigChatBrasil.domain.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Representa um item dentro da fila de processamento.
 * Guarda a mensagem e sua prioridade.
 */
@Getter
@AllArgsConstructor
public class MessageQueueItem {

    private final Message message;
    private final boolean urgent;
    private final Long sequence;
}
