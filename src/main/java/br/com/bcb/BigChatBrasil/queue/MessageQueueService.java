package br.com.bcb.BigChatBrasil.queue;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Serviço responsável por gerenciar a fila de mensagens em memória.
 * 
 * Estratégia:
 * - Duas filas: urgent e normal
 * - FIFO dentro de cada prioridade
 * - Anti-starvation: a cada 3 urgentes, força 1 normal
 */
@Service
public class MessageQueueService {

    private final Queue<MessageQueueItem> urgentQueue = new LinkedList<>();
    private final Queue<MessageQueueItem> normalQueue = new LinkedList<>();

    private int urgentProcessedCounter = 0;

    /**
     * Obtém o próximo item da fila com lógica de priorização
     */
    public synchronized MessageQueueItem dequeueNext() {

        // Anti-starvation:
        // A cada 3 urgentes processadas, força 1 normal
        if (urgentProcessedCounter >= 3 && !normalQueue.isEmpty()) {
            urgentProcessedCounter = 0;
            return normalQueue.poll();
        }

        // Prioriza urgent
        if (!urgentQueue.isEmpty()) {
            urgentProcessedCounter++;
            return urgentQueue.poll();
        }

        // Se não houver urgent, pega normal
        if (!normalQueue.isEmpty()) {
            return normalQueue.poll();
        }

        return null;
    }

    public synchronized int getUrgentSize() {
        return urgentQueue.size();
    }

    public synchronized int getNormalSize() {
        return normalQueue.size();
    }

    public synchronized int getTotalSize() {
        return urgentQueue.size() + normalQueue.size();
    }
}
