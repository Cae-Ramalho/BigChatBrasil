package br.com.bcb.BigChatBrasil.controller;

import br.com.bcb.BigChatBrasil.dto.request.SendMessageRequestDTO;
import br.com.bcb.BigChatBrasil.dto.response.SendMessageResponseDTO;
import br.com.bcb.BigChatBrasil.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de envio de mensagens
 *
 * Identificação do cliente via clientId (requisito do desafio)
 */
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * Envia mensagem para fila
     */
    @PostMapping
    public SendMessageResponseDTO sendMessage(
            @RequestParam Long clientId,
            @RequestBody SendMessageRequestDTO dto) {

        return messageService.sendMessage(clientId, dto);
    }

    /**
     * Consulta status da mensagem
     */
    @GetMapping("/{id}/status")
    public String getStatus(@PathVariable Long id) {
        return messageService.getStatus(id).name();
    }
}
