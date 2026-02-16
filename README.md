# BACKEND - Teste-Tecnico-BigChatBrasil

GitURL: https://github.com/Cae-Ramalho/BigChatBrasil.git

BigChatBrasil – Sistema de Filas para Processamento de Mensagens

# Visão Geral

Este projeto é uma API backend desenvolvida em Java + Spring Boot, com foco na implementação de um sistema de filas para processamento de mensagens de chat com priorização, conforme proposto no desafio técnico Backend BCB.
O objetivo principal é simular um sistema de envio de mensagens entre clientes e destinatários, aplicando:
Validação financeira baseada em plano (pré-pago e pós-pago)
Processamento de mensagens via fila em memória
Priorização de mensagens (normal / urgente)
Organização em camadas seguindo boas práticas de arquitetura
A aplicação está containerizada com Docker, permitindo execução simples em qualquer ambiente.

# Tecnologias Utilizadas
- Java 17
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- Docker / Docker Compose
- Lombok
- Hibernate ORM

# Arquitetura e Organização
- O projeto segue uma arquitetura em camadas:

    controller → service → domain(entity) → repository → database
                          ↓
                        queue (fila em memória)


Principais camadas:
- controller: Endpoints REST
- service: Regras de negócio
- domain.entity: Modelagem do domínio
- repository: Persistência com JPA
- queue: Implementação da fila de mensagens
- dto: Objetos de entrada e saída da API

Funcionalidades Implementadas
- ✔ Parte 1 – Funcionalidades Essenciais
    Clientes
    Cadastro de clientes
    Validação de documento único (CPF/CNPJ)
    Controle de plano:
        Pré-pago → saldo
        Pós-pago → limite e consumo mensal
    Mensagens
        Envio de mensagens
        Cálculo automático de custo:
        - Normal: R$ 0.25
        - Urgente: R$ 0.50

    Validação financeira:
        Verifica saldo (pré-pago)
        Verifica limite (pós-pago)
        Fila de Mensagens (em memória)
        Implementação de fila FIFO
        Enfileiramento síncrono
        Registro de status das mensagens:
        queued
        processing
        delivered
        failed

Parte 2 – Aprimoramentos
    Priorização de mensagens:
    Fila normal
    Fila urgente
    Mensagens urgentes são processadas antes das normais
    Estrutura preparada para evitar starvation
    Identificação do cliente via clientId na requisição

Limitações Atuais
    Autenticação simplificada (sem JWT)
    Processamento principal ainda síncrono (worker futuro)
    Monitoramento avançado de fila não implementado
    Sem cache distribuído
    Sem testes automatizados (ponto de melhoria)

# Estrutura de Dados
CLIENT =
- ID_CLIENT BIGINT (PRIMARY KEY) --Código do Cliente
- NAME_CLIENT VARCHAR(150) --Nome do Cliente
- DOCUMENT_CLIENT VARCHAR(15) --Número de CPF ou CNPJ (Único)
- DOCUMENT_TYPE_CLIENT ENUM --Tipo de Documento ("CPF" ou "CNPJ")
- PLAN_TYPE_CLIENT ENUM --Tipo de Plano do Cliente ("prepaid" ou "postpaid")
- BALANCE_CLIENT NUMERIC(15,4) --Saldo para cliente PréPago
- LIMIT_CLIENT NUMERIC(15,4) --Limite de Crédito para PósPago
- BO_ACTIVE_CLIENT BOOLEAN --Cliente ativo (True ou False)
- MONTHLY_USAGE NUMERIC(15,4) --Valor de uso no mês atual
- LAST_RESET TIMESTAMP --Última Reinicialçização de crédito (PósPago)	
- DT_CREATE TIMESTAMP --Data/hora de criação do usuário
	
MESSAGE = 
- ID_MESSAGE BIGINT (PRIMARY KEY) --Código da Mensagem
- ID_CLIENT BIGINT (FOREIGN KEY) --Código do cliente
- ID_CONVERSATION BIGINT (FOREIGN KEY) --Código da Conversa
- ID_RECIPIENT BIGINT (FOREIGN KEY) --Telefone de Contato do Destinatário 
- CONTENT_MESSAGE TEXT --Conteúdo da Mensagem
- DATE_SEND_MESSAGE TIMESTAMP --Data que o Usuário requisitou o Envio da Mensagem
- PRIORITY_MESSAGE ENUM --Prioridade da Mensagem ("normal" ou "urgent")
- STATUS_MESSAGE ENUM --Status de processamento da mensagem ("queued", "processing", "sent", "delivered", "read", "failed")
- COST_MESSAGE NUMERIC(15,4) --Valor de custo da Mensagem (0.25 normal, 0.50 urgente)
- DATE_DELIVERED TIMESTAMP --Data/hora de entrega da mensagem
- DATE_PROCESSED TIMESTAMP --Data/hora de Processamento da Mensagem
- DATE_FAILED TIMESTAMP --Data/hora de falha (Caso houver)
	
CONVERSATION = 
- ID_CONVERSATION	BIGINT (PRIMARY KEY) --Código da Conversa
- ID_CLIENT BIGINT (FOREIGN KEY) --Código do Cliente
- ID_RECIPIENT BIGINT (FOREIGN KEY) --Telefone de Contato do Destinatário
- LAST_MESSAGE_CONTENT TEXT --Conteúdo da última mensagem
- LAST_MESSAGE_TIME TIMESTAMP --Data da última Mensagem
- UNREAD_COUNT BIGINT --Contador da quantidade de mensagens não lidas
- STATUS_CONVERSATION ENUM --Status da Conversa ("open", "closed")
	
FINANCIAL_TRANSACTION = 
- ID_FINANCIAL_TRANSACTION BIGINT (PRIMARY KEY) --Código da Transação
- ID_CLIENT BIGINT (FOREIGN KEY) --Código do Cliente
- ID_MESSAGE BIGINT (FOREIGN KEY) --Código da Mensagem
- ID_CONVERSATION BIGINT (FOREIGN KEY) --Código da Conversa
- DATE_TRANSACTION TIMESTAMP --Data/hora da Transação
- BALANCE_BEFORE	NUMERIC(15,4) --Valor Total de Saldo, ou Crédito, antes da mensagem
- BALANCE_AFTER	NUMERIC(15,4) --Valor Total de Saldo, ou Crédito, depois da mensagem
	
RECIPIENT = 
- ID_RECIPIENT BIGINT (PRIMARY KEY) --Código do Destinatário
- NAME_RECIPIENT VARCHAR(150) --Nome do Destinatário
- RECIPIENT_CONTACT_PHONE VARCHAR(20) --Telefone de Contato do Destinatário	
- BO_ACTIVE_RECIPIENT BOOLEAN --Destinatário ativo (True ou False)
- DATE_CREATE TIMESTAMP --Data de Criação do Destinatário
- AVAILABLE BOOLEAN --Define se o destinatário está disponível para novas mensagens
	
USER =
- ID_USER BIGINT (PRIMARY KEY) --Código do usuário
- NAME_USER VARCHAR(150) --Nome do Usuário
- USER_TYPE ENUM --Tipo de Usuário ("admin", "regular")
- EMAIL_USER VARCHAR(150) --E-mail do usuário 
- PASSWORD_HASH VARCHAR(255) --Senha do Usuário, para autenticação
- BO_ACTIVE_USER BOOLEAN --Usuário ativo (True ou False)
- DATE_CREATE TIMESTAMP --Data de Criação do Usuário
	
LOG_USER = 
- ID_LOG_USER BIGINT (PRIMARY KEY) --Código do Log	
- ID_USER BIGINT (FOREIGN KEY) --Código do usuário que executou a ação
- ACTION_TYPE ENUM --Tipo de ação executada ("insert", "edit", "exclude")
- ACTION VARCHAR(255) --Ação Executada
- DATE_ACTION TIMESTAMP --Data/hora de execução da ação

# Endpoints Disponíveis
    Clientes
        Criar cliente
        POST /clients

        Exemplo Request
        {
        "nameClient": "Empresa ABC",
        "documentClient": "12345678000199",
        "documentTypeClient": "CNPJ",
        "planTypeClient": "prepaid"
        }

    Mensagens
        Enviar mensagem
        POST /messages?clientId={id}

        Request
        {
        "recipientId": 1,
        "content": "Olá, como vai?",
        "priority": "normal"
        }

        Response
        {
        "id": 1,
        "status": "queued",
        "estimatedDelivery": "2026-02-16T10:00:05",
        "cost": 0.25
        }

        Consultar status da mensagem
        GET /messages/{id}/status

# Executando com Docker
    Pré-requisitos
    - Docker Desktop

    Passos
    - docker-compose up --build ou docker-compose up

    A aplicação será iniciada em:
    - http://localhost:8080/swagger-ui/index.html#/

    O banco PostgreSQL será criado automaticamente via container.

# Banco de Dados
- Banco utilizado: PostgreSQL
- As tabelas são criadas automaticamente via JPA/Hibernate com base nas entidades do domínio.

# Limitações Conhecidas
- Worker assíncrono completo ainda não implementado
- Falta endpoint de monitoramento da fila
- Sem autenticação JWT (identificação via clientId)
- Sem testes automatizados

# Melhorias Futuras
- Worker assíncrono com thread dedicada
- Endpoint /queue/status
- Autenticação JWT para clientes
- Dashboard de monitoramento
- Cache para otimização de performance
- Testes unitários e de integração
