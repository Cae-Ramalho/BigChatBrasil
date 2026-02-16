package br.com.bcb.BigChatBrasil.controller;

import br.com.bcb.BigChatBrasil.queue.MessagePriorityQueue;
import br.com.bcb.BigChatBrasil.queue.QueueStats;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Endpoint exigido pelo desafio:
 * GET /queue/status
 */
@RestController
@RequestMapping("/queue")
@RequiredArgsConstructor
public class QueueController {

    private final MessagePriorityQueue queue;
    private final QueueStats stats;

    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of(
                "totalInQueue", queue.size(),
                "urgent", queue.urgentSize(),
                "normal", queue.normalSize(),
                "processed", stats.getProcessed().get(),
                "failed", stats.getFailed().get()
        );
    }
}
