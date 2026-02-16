package br.com.bcb.BigChatBrasil.controller;

import br.com.bcb.BigChatBrasil.dto.request.ClientRequestDTO;
import br.com.bcb.BigChatBrasil.dto.response.ClientResponseDTO;
import br.com.bcb.BigChatBrasil.service.ClientService;
import lombok.RequiredArgsConstructor;

import org.hibernate.service.spi.ServiceException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ClientResponseDTO create(@RequestBody ClientRequestDTO dto) throws ServiceException {
        return clientService.createClient(dto);
    }    
    
}
