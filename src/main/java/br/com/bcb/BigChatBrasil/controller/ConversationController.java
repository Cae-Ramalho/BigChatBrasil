package br.com.bcb.BigChatBrasil.controller;

import br.com.bcb.BigChatBrasil.dto.response.ConversationResponseDTO;
import br.com.bcb.BigChatBrasil.dto.response.MessageHistoryResponseDTO;
import br.com.bcb.BigChatBrasil.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints obrigatórios do desafio:
 * - Listar conversas do cliente
 * - Obter conversa
 * - Histórico de mensagens
 */
@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    /**
     * GET /conversations?clientId=1
     */
    @GetMapping
    public List<ConversationResponseDTO> list(
            @RequestParam Long clientId) {

        return conversationService.listByClient(clientId);
    }

    /**
     * GET /conversations/{id}
     */
    @GetMapping("/{id}")
    public ConversationResponseDTO get(@PathVariable Long id) {
        return conversationService.getConversation(id);
    }

    /**
     * GET /conversations/{id}/messages
     */
    @GetMapping("/{id}/messages")
    public List<MessageHistoryResponseDTO> history(@PathVariable Long id) {
        return conversationService.listMessages(id);
    }
}
