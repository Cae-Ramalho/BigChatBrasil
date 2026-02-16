package br.com.bcb.BigChatBrasil.service;

import br.com.bcb.BigChatBrasil.domain.entity.Client;
import br.com.bcb.BigChatBrasil.domain.entity.Conversation;
import br.com.bcb.BigChatBrasil.domain.entity.Message;
import br.com.bcb.BigChatBrasil.dto.response.ConversationResponseDTO;
import br.com.bcb.BigChatBrasil.dto.response.MessageHistoryResponseDTO;
import br.com.bcb.BigChatBrasil.repository.ClientRepository;
import br.com.bcb.BigChatBrasil.repository.ConversationRepository;
import br.com.bcb.BigChatBrasil.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ClientRepository clientRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    /**
     * Lista conversas de um cliente
     */
    public List<ConversationResponseDTO> listByClient(Long clientId) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ServiceException("Cliente não encontrado"));

        return conversationRepository.findByClient(client)
                .stream()
                .map(conv -> ConversationResponseDTO.builder()
                        .idConversation(conv.getIdConversation())
                        .clientId(client.getIdClient())
                        .recipientId(conv.getRecipient().getIdRecipient())
                        .recipientName(conv.getRecipient().getNameRecipient())
                        .lastMessageContent(conv.getLastMessageContent())
                        .lastMessageTime(conv.getLastMessageTime())
                        .unreadCount(conv.getUnreadCount())
                        .statusConversation(conv.getStatusConversation())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Detalhe de uma conversa específica
     */
    public ConversationResponseDTO getConversation(Long id) {

        Conversation conv = conversationRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Conversa não encontrada"));

        return ConversationResponseDTO.builder()
                .idConversation(conv.getIdConversation())
                .clientId(conv.getClient().getIdClient())
                .recipientId(conv.getRecipient().getIdRecipient())
                .recipientName(conv.getRecipient().getNameRecipient())
                .lastMessageContent(conv.getLastMessageContent())
                .lastMessageTime(conv.getLastMessageTime())
                .unreadCount(conv.getUnreadCount())
                .statusConversation(conv.getStatusConversation())
                .build();
    }

    /**
     * Histórico de mensagens da conversa
     */
    public List<MessageHistoryResponseDTO> listMessages(Long conversationId) {

        Conversation conv = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ServiceException("Conversa não encontrada"));

        List<Message> messages =
                messageRepository.findByConversationOrderByDateSendMessageAsc(conv);

        return messages.stream()
                .map(msg -> MessageHistoryResponseDTO.builder()
                        .idMessage(msg.getIdMessage())
                        .content(msg.getContentMessage())
                        .priority(msg.getPriorityMessage())
                        .status(msg.getStatusMessage())
                        .cost(msg.getCostMessage())
                        .sentAt(msg.getDateSendMessage())
                        .deliveredAt(msg.getDateDelivered())
                        .build())
                .collect(Collectors.toList());
    }
}
