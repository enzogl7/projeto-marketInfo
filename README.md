<h1>Projeto MarketInfo 🛒</h1>
<img src="https://enzogl7.github.io/portfolio/assets/Screenshot_marketInfo.png">

Aplicação web desenvolvida para gerenciar as operações essenciais de supermercados, proporcionando uma interface intuitiva e recursos completos para controlar produtos, estoques e preços, além de ferramentas administrativas robustas para gerenciar categorias, perfis de usuários e permissões de acesso.

- Principais Funcionalidades:
  - Sistema de Autenticação: Login seguro e criação de contas para controle de acesso.
  - Gerenciamento de Produtos: Cadastro, listagem, edição e exclusão de produtos.
  - Controle de Estoques: Registro, edição, listagem e exclusão de estoques vinculados a produtos.
  - Gestão de Preços: Cadastro, edição, listagem e exclusão de preços de produtos.

- Funções administrativas:
  - Administração de Categorias: Criação, listagem, edição e exclusão de categorias para organização de produtos.
  - Gerenciamento de Perfis: Controle de permissões (roles) com criação, edição e exclusão de perfis de usuário.
  - Listagem e Edição de Usuários: Permite que administradores editem dados dos usuários, como nome, e-mail, senha e roles atribuídas, além de ativar ou inativar usuários conforme necessário.

- Funcionalidade de Mensageria e Notificações:
  - Integração com Kafka: O sistema utiliza o Apache Kafka para processar mensagens de estoque, como alertas de baixo estoque ou atualização de produtos.
  - Envio de E-mails Automatizado: A aplicação envia e-mails para uma lista de destinatários definida, utilizando JavaMailSender. Os e-mails são enviados com conteúdo HTML gerado dinamicamente, incluindo o texto da mensagem recebida via Kafka.
  - Templates de E-mail HTML: Mensagens de estoque e alertas são formatadas em páginas HTML para melhor visualização pelos destinatários.

- Qualidade de Software:
  - Testes unitários implementados com JUnit e Mockito para validar a lógica de negócio e garantir a integridade das operações essenciais.
  - Possui documentação detalhada da API com Swagger para facilitar a integração e o entendimento dos endpoints.

- Tecnologias Utilizadas:
   - Backend: Spring Boot (Java), Hibernate (ORM)
   - Banco de Dados: PostgreSQL, JPA
   - Frontend: Thymeleaf, HTML, CSS, JavaScript
   - Segurança: Spring Security para autenticação e controle de permissões

<a href="https://youtu.be/-JzQapylqmw">Demonstração da aplicação em vídeo<a>
<br>
<a href="https://prnt.sc/Z4S9PVqwm1c6">Exemplo de e-mail enviado automaticamente<a>
