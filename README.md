**Projeto OPALA - OPALA is a PArking Lot Assistant**


## ⚠️ PROJETO EM DESENVOLVIMENTO ⚠️
A descrição abaixo expõe os recursos planejados até o fim do projeto, mas que podem ainda não ter sido implementados.


## Descrição

OPALA, que significa "**O**PALA is a **PA**rking **L**ot **A**ssistant," é um sistema de gerenciamento para estacionamentos desenvolvido em Java utilizando Maven como gerenciador de dependências e MySQL/MariaDB como banco de dados.

Este projeto foi desenvolvido como trabalho final da disciplina de Programação Orientada a Objetos do Instituto Federal do Norte de Minas Gerais (IFNMG), campus Montes Claros, e tem como objetivo oferecer uma solução eficiente e intuitiva para o gerenciamento de estacionamentos, proporcionando controle sobre a entrada e saída de veículos, gestão de usuários com diferentes permissões e acomodando tanto horistas quanto mensalistas.

## Funcionalidades

- **Cadastro de Usuários:** Cadastre usuários com diferentes permissões, incluindo gerentes e mensalistas/assinantes.

- **Cadastro de Veículos:** Associe veículos aos usuários por meio do registro da placa e modelo.

- **Controle de Entrada e Saída:** Registre e acompanhe a entrada e saída de veículos no estacionamento.

- **Cálculo de Pagamento:** Realize o cálculo automático do valor a ser pago com base no tempo de permanência para horistas, e periodicamente para mensalistas/assinantes.

- **Relatórios Diários e Mensais:** Gere relatórios detalhados ao final do expediente e ao fim de cada mês para análise e contabilidade.

## Tecnologias Utilizadas

- **Java:** Linguagem de programação principal.
- **Maven:** Gerenciador de dependências para facilitar a construção e gestão do projeto.
- **MySQL/MariaDB:** Banco de dados relacional para armazenamento persistente de dados.

## Como Executar

1. Clone o repositório para sua máquina local.
2. Configure a conexão com o banco de dados no arquivo de configuração.
3. Execute o projeto usando o Maven.

```bash
mvn clean install
java -jar target/opala.jar
```

## Contribuições

Contribuições são bem-vindas! Se você encontrar problemas ou tiver sugestões de melhorias, sinta-se à vontade para abrir uma *issue* ou enviar um *pull request*.
