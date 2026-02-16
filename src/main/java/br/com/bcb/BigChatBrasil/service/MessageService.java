package br.com.bcb.BigChatBrasil.service;

import br.com.bcb.BigChatBrasil.domain.entity.*;
import br.com.bcb.BigChatBrasil.domain.enums.StatusMessage;
import br.com.bcb.BigChatBrasil.dto.request.SendMessageRequestDTO;
import br.com.bcb.BigChatBrasil.dto.response.SendMessageResponseDTO;
import br.com.bcb.BigChatBrasil.queue.MessagePriorityQueue;
import br.com.bcb.BigChatBrasil.queue.MessageQueueItem;
import br.com.bcb.BigChatBrasil.repository.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ClientRepository clientRepository;
    private final RecipientRepository recipientRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final MessagePriorityQueue queue;

    /**
     * Envia mensagem e coloca na fila assíncrona
     */
    public SendMessageResponseDTO sendMessage(Long clientId,
                                              SendMessageRequestDTO dto) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ServiceException("Cliente não encontrado"));

        Recipient recipient = recipientRepository.findById(dto.getRecipientId())
                .orElseThrow(() -> new ServiceException("Destinatário não encontrado"));

        if (!recipient.canReceiveMessage()) {
            throw new ServiceException("Destinatário indisponível");
        }

        Conversation conversation =
                conversationRepository.findByClientAndRecipient(client, recipient)
                        .orElseGet(() -> conversationRepository.save(
                                Conversation.builder()
                                        .client(client)
                                        .recipient(recipient)
                                        .build()
                        ));

        Message message = Message.builder()
                .client(client)
                .recipient(recipient)
                .conversation(conversation)
                .contentMessage(dto.getContent())
                .priorityMessage(dto.getPriority())
                .build();

        message = messageRepository.save(message);

        // Regra financeira do domínio
        client.charge(message.getCostMessage());
        clientRepository.save(client);

        // Atualiza conversa
        conversation.registerNewMessage(message.getContentMessage());
        conversationRepository.save(conversation);

        // Enfileira para worker assíncrono
        queue.enqueue(new MessageQueueItem(
                message,
                message.getPriorityMessage().name().equals("urgent"), clientId
        ));

        return SendMessageResponseDTO.builder()
                .id(message.getIdMessage())
                .status(message.getStatusMessage())
                .estimatedDelivery(LocalDateTime.now().plusSeconds(5))
                .cost(message.getCostMessage())
                .build();
    }

    /**
     * Consulta status da mensagem
     */
    public StatusMessage getStatus(Long messageId) {
        return messageRepository.findById(messageId)
                .map(Message::getStatusMessage)
                .orElseThrow(() -> new ServiceException("Mensagem não encontrada"));
    }
}
