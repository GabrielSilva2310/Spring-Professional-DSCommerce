# DSCommerce 
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/GabrielSilva2310/Spring-Professional-DSCommerce/blob/main/LICENSE) 

# Sobre o projeto
É uma aplicação Back end, e o projeto principal da trilha do curso Java Spring Professional da [DevSuperior](https://devsuperior.com.br "Site da DevSuperior").
O projeto consiste em um sistema de comércio eletrônico onde é possível realizar funcionalidades tais como manter um
cadastro de produtos, pesquisar produtos para fins de catálogo e carrinho de compras, registrar
e recuperar pedidos, consultas ao banco de dados, login, controle de acesso, dentre outras.
Desenvolvido ao longo do curso esse projeto é utilizado para aplicar os tópicos de conteúdo abordados nos módulos do curso, como:
Componentes e injeção de dependência, Modelo de Domínio e ORM, API REST, camadas, CRUD, exceções, validações, JPA, consultas SQL e JPQL, 
Login e controle de acesso.


## Modelo conceitual
Este é o modelo conceitual do Sistema DSCommerce. Considerações:

-Cada item de pedido (OrderItem) corresponde a um produto no pedido, com uma 
 quantidade. Sendo que o preço também é armazenado no item de pedido por 
 questões de histórico (se o preço do produto mudar no futuro, o preço do item de 
 pedido continua registrado com o preço real que foi vendido na época).

-Um usuário pode ter um ou mais "roles", que são os perfis de acesso deste usuário 
 no sistema (client, admin).
![Modelo Conceitual](https://github.com/GabrielSilva2310/Assets/blob/main/Image%20DSCommerce/Domain%20Model.png)

# Tecnologias utilizadas
- Java 17
- Spring Boot 3
- Maven
- JPA / Hibernate
- Spring Security
- OAuth2
- JWT
- H2 Database
- Postman


# Como executar o projeto

Pré-requisitos: Java 17

```bash
# clonar repositório
git clone https://github.com/GabrielSilva2310/Spring-Professional-DSCommerce.git

# entrar na pasta do projeto
cd Spring-Professional-DSCommerce

# executar o projeto
./mvnw spring-boot:run
```

# Autor

Gabriel Da Silva 

www.linkedin.com/in/gabriel-da-silva-457039193
