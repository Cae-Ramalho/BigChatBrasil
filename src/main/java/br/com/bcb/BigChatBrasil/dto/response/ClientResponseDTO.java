package br.com.bcb.BigChatBrasil.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class ClientResponseDTO {
    private Long idClient;
    private String nameClient;
    private String documentClient;
    private BigDecimal balanceClient;
    private BigDecimal limitClient;
    private Boolean boActiveClient;    
    
}
