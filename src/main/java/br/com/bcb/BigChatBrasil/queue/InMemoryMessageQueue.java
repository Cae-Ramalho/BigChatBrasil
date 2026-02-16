package br.com.bcb.BigChatBrasil.queue;

import br.com.bcb.BigChatBrasil.domain.enums.PriorityMessage;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Fila em memória com priorização:
 *
 * - Fila NORMAL (FIFO)
 * - Fila URGENT (prioridade)
 *
 * Regras:
 * 1) Urgentes são processadas primeiro
 * 2) Anti-starvation: a cada 5 urgentes, processa 1 normal
 */
@Component
public class InMemoryMessageQueue {

    private final Queue<MessageQueueItem> normalQueue = new LinkedList<>();
    private final Queue<MessageQueueItem> urgentQueue = new LinkedList<>();

    private final AtomicLong sequenceGenerator = new AtomicLong(0);

    private long urgentCounter = 0;
    private long processedMessages = 0;
    private long failedMessages = 0;

    /**
     * Adiciona mensagem na fila correta
     */
    public synchronized void enqueue(MessageQueueItem item, PriorityMessage priority) {

        if (priority == PriorityMessage.urgent) {
            urgentQueue.add(item);
        } else {
            normalQueue.add(item);
        }
    }

    /**
     * Retira próximo item respeitando:
     * - prioridade urgente
     * - anti-starvation (5 urgentes -> 1 normal)
     */
    public synchronized MessageQueueItem dequeue() {

        if (urgentQueue.isEmpty() && normalQueue.isEmpty()) {
            return null;
        }

        if (!urgentQueue.isEmpty() && (urgentCounter < 5 || normalQueue.isEmpty())) {
            urgentCounter++;
            return urgentQueue.poll();
        }

        urgentCounter = 0;
        return normalQueue.poll();
    }

    public long nextSequence() {
        return sequenceGenerator.incrementAndGet();
    }

    public synchronized void incrementProcessed() {
        processedMessages++;
    }

    public synchronized void incrementFailed() {
        failedMessages++;
    }

    public synchronized int getNormalQueueSize() {
        return normalQueue.size();
    }

    public synchronized int getUrgentQueueSize() {
        return urgentQueue.size();
    }

    public long getProcessedMessages() {
        return processedMessages;
    }

    public long getFailedMessages() {
        return failedMessages;
    }
}
