package br.com.bcb.BigChatBrasil.dto.request;

import br.com.bcb.BigChatBrasil.domain.enums.DocumentType;
import br.com.bcb.BigChatBrasil.domain.enums.PlanType;
import lombok.Data;

@Data
public class ClientRequestDTO {
    private String nameClient;
    private String documentClient;
    private DocumentType documentTypeClient;
    private PlanType planTypeClient;    
}
