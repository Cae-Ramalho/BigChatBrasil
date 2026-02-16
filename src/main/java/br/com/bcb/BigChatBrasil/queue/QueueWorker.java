package br.com.bcb.BigChatBrasil.queue;

import br.com.bcb.BigChatBrasil.domain.entity.Message;
import br.com.bcb.BigChatBrasil.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Worker assíncrono:
 * Processa mensagens continuamente em background
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class QueueWorker {

    private final MessagePriorityQueue queue;
    private final MessageRepository messageRepository;
    private final QueueStats stats;

    /**
     * Executa continuamente a cada 2 segundos
     */
    @Scheduled(fixedDelay = 2000)
    public void processQueue() {

        MessageQueueItem item = queue.dequeue();

        if (item == null)
            return;

        Message msg = item.getMessage();

        try {
            log.info("Processando mensagem {}", msg.getIdMessage());

            msg.markProcessing();
            messageRepository.save(msg);

            // Simulação de envio externo
            Thread.sleep(500);

            msg.markDelivered();
            messageRepository.save(msg);

            stats.incrementProcessed();

        } catch (Exception ex) {
            msg.markFailed();
            messageRepository.save(msg);
            stats.incrementFailed();
        }
    }
}
