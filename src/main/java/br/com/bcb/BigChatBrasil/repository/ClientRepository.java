package br.com.bcb.BigChatBrasil.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bcb.BigChatBrasil.domain.entity.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByDocumentClient(String documentClient);
    
}
