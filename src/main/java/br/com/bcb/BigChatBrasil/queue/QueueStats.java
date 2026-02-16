package br.com.bcb.BigChatBrasil.queue;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Métricas da fila para monitoramento
 */
@Getter
@Component
public class QueueStats {

    private final AtomicLong processed = new AtomicLong(0);
    private final AtomicLong failed = new AtomicLong(0);

    public void incrementProcessed() {
        processed.incrementAndGet();
    }

    public void incrementFailed() {
        failed.incrementAndGet();
    }
}
