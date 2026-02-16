package br.com.bcb.BigChatBrasil.service;

import br.com.bcb.BigChatBrasil.domain.entity.Client;
import br.com.bcb.BigChatBrasil.domain.enums.PlanType;
import br.com.bcb.BigChatBrasil.dto.request.ClientRequestDTO;
import br.com.bcb.BigChatBrasil.dto.response.ClientResponseDTO;
import br.com.bcb.BigChatBrasil.repository.ClientRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientResponseDTO createClient(ClientRequestDTO dto) throws ServiceException {

        if (clientRepository.findByDocumentClient(dto.getDocumentClient()).isPresent()) {
            throw new ServiceException("Documento já cadastrado");
        }

        Client client = Client.builder()
                .nameClient(dto.getNameClient())
                .documentClient(dto.getDocumentClient())
                .documentTypeClient(dto.getDocumentTypeClient())
                .planTypeClient(dto.getPlanTypeClient())
                .build();

        client = clientRepository.save(client);

        return ClientResponseDTO.builder()
                .idClient(client.getIdClient())
                .nameClient(client.getNameClient())
                .documentClient(client.getDocumentClient())
                .balanceClient(client.getBalanceClient())
                .limitClient(client.getLimitClient())
                .boActiveClient(client.getBoActiveClient())
                .build();
    }

    public Client findByDocument(String document) {
        return clientRepository.findByDocumentClient(document)
                .orElseThrow(() -> new ServiceException("Cliente não encontrado"));
    }
    

    public void registerUsage(String document, BigDecimal amount) {

        Client client = findByDocument(document);

        if (!client.getBoActiveClient()) {
            throw new ServiceException("Cliente inativo");
        }

        if (client.getPlanTypeClient() == PlanType.prepaid) {

            if (client.getBalanceClient().compareTo(amount) < 0) {
                throw new ServiceException("Saldo insuficiente");
            }

            client.setBalanceClient(client.getBalanceClient().subtract(amount));
        }

        if (client.getPlanTypeClient() == PlanType.postpaid) {

            BigDecimal total = client.getMonthlyUsage().add(amount);

            if (total.compareTo(client.getLimitClient()) > 0) {
                throw new ServiceException("Limite excedido");
            }

            client.setMonthlyUsage(total);
        }

        clientRepository.save(client);
    }    

    
}
