package br.com.bcb.BigChatBrasil.queue;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fila em memória com priorização:
 * - Urgent sempre priorizada
 * - Anti-starvation: após X urgentes, processa 1 normal
 */
@Component
public class MessagePriorityQueue {

    private final Queue<MessageQueueItem> urgentQueue = new LinkedList<>();
    private final Queue<MessageQueueItem> normalQueue = new LinkedList<>();

    private final AtomicInteger processedUrgents = new AtomicInteger(0);

    private static final int STARVATION_LIMIT = 3;

    public synchronized void enqueue(MessageQueueItem item) {
        if (item.isUrgent()) {
            urgentQueue.add(item);
        } else {
            normalQueue.add(item);
        }
    }

    public synchronized MessageQueueItem dequeue() {

        if (!urgentQueue.isEmpty()
                && processedUrgents.get() < STARVATION_LIMIT) {

            processedUrgents.incrementAndGet();
            return urgentQueue.poll();
        }

        if (!normalQueue.isEmpty()) {
            processedUrgents.set(0);
            return normalQueue.poll();
        }

        if (!urgentQueue.isEmpty()) {
            return urgentQueue.poll();
        }

        return null;
    }

    public synchronized int size() {
        return urgentQueue.size() + normalQueue.size();
    }

    public synchronized int urgentSize() {
        return urgentQueue.size();
    }

    public synchronized int normalSize() {
        return normalQueue.size();
    }
}
