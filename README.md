# Nome do projeto
Sistema de Moeda Estudantil.

## Integrantes
* Henrique Lima Volponi
* João Vitor Ferreira Jacinto
* Pedro Henrique Novais Baranda

## Orientadores
* João Pedro Oliveira Batisteli

## Instruções de utilização
Assim que a primeira versão do sistema estiver disponível, deverá complementar com as instruções de utilização. Descreva como instalar eventuais dependências e como executar a aplicação.

---

# Histórias de Usuário

### Épico 1: Gestão e Configuração (Funcionalidades da Instituição/Operação)

**US01: Gerenciar Instituições Participantes**

* Como um **Operador**,
* Eu quero **criar, visualizar, editar e inativar instituições participantes**,
* Para que **os alunos se vinculem apenas a instituições válidas e pré-cadastradas**.

**US02: Gerenciar Professores Pré-Cadastrados**

* Como um **Operador**,
* Eu quero **importar e manter a lista de professores (nome, CPF, departamento, instituição)**,
* Para que **os docentes possam ativar suas contas e distribuir moedas**.

**US03: Gerenciar Parceiros e Vantagens (Curadoria/Aprovação)**

* Como um **Operador**,
* Eu quero **aprovar ou inativar parceiros e revisar vantagens cadastradas (título, descrição, foto, custo em moedas)**,
* Para que **o catálogo mantenha qualidade e conformidade**.

**US04: Gerenciar Catálogo de Vantagens (Parceiro)**

* Como um **Parceiro**,
* Eu quero **criar, visualizar, editar e inativar vantagens com título, descrição, foto e custo em moedas**,
* Para que **os alunos tenham ofertas atualizadas para resgate**.

**US21: Cadastrar-se como Parceiro**

* Como um **Parceiro (empresa)**,
* Eu quero **me cadastrar informando CNPJ, e-mail e dados de contato para posterior aprovação**,
* Para que **eu possa oferecer vantagens aos alunos no sistema**.

### Épico 2: Jornada do Aluno

**US05: Cadastrar-se no Sistema (Aluno)**

* Como um **Aluno**,
* Eu quero **me cadastrar informando nome, e-mail, CPF, RG, endereço, instituição (seleção em lista pré-cadastrada) e curso**,
* Para que **eu possa participar do sistema de mérito**.

**US06: Realizar Login no Sistema (Usuário)**

* Como um **Usuário (Aluno, Professor ou Parceiro)**,
* Eu quero **fazer login usando meu e-mail e senha**,
* Para que **eu acesse as funcionalidades correspondentes ao meu perfil**.

**US07: Consultar Meus Extratos (Aluno)**

* Como um **Aluno**,
* Eu quero **visualizar meu saldo de moedas e o histórico de recebimentos e resgates**,
* Para que **eu acompanhe minhas transações e planeje novos resgates**.

**US08: Resgatar Vantagem (Aluno)**

* Como um **Aluno**,
* Eu quero **selecionar uma vantagem do catálogo e concluir o resgate com débito do meu saldo e geração de um código único com validade**,
* Para que **eu possa usufruir do benefício e comprovar a troca presencialmente**.

### Épico 3: Distribuição de Moedas (Professor)

**US09: Receber Crédito Semestral Acumulável**

* Como um **Professor**,
* Eu quero **receber automaticamente 1.000 moedas a cada semestre, somando ao meu saldo caso não as utilize integralmente**,
* Para que **eu disponha de recursos contínuos para reconhecer meus alunos**.

**US10: Enviar Moedas para Aluno (com motivo obrigatório)**

* Como um **Professor**,
* Eu quero **enviar moedas a um aluno da minha instituição indicando a quantidade e um motivo obrigatório**,
* Para que **eu reconheça bom comportamento, participação e desempenho acadêmico**.

**US11: Consultar Meu Extrato (Professor)**

* Como um **Professor**,
* Eu quero **visualizar meu saldo e o histórico de créditos semestrais e envios realizados**,
* Para que **eu acompanhe e audite a distribuição de moedas**.

**US22: Visualizar Vínculo Institucional (Professor)**

* Como um **Professor**,
* Eu quero **ver de forma explícita a instituição e o departamento aos quais estou vinculado**,
* Para que **eu confirme meu vínculo e atue dentro do escopo correto**.

### Épico 4: Processos e Notificações (Funcionalidades do Sistema)

**US12: Notificar Aluno sobre Recebimento de Moedas**

* Como o **Sistema**,
* Eu quero **enviar e-mail ao aluno sempre que ele receber moedas (com remetente, quantidade e motivo)**,
* Para que **ele seja informado do reconhecimento**.

**US13: Enviar Cupom de Resgate ao Aluno**

* Como o **Sistema**,
* Eu quero **enviar ao aluno um e-mail com o cupom do resgate (código único e validade)**,
* Para que **ele utilize o benefício na troca presencial**.

**US14: Notificar Parceiro sobre Resgate**

* Como o **Sistema**,
* Eu quero **enviar ao parceiro um e-mail com o mesmo código do cupom e dados do resgate**,
* Para que **ele possa conferir e registrar a utilização**.

**US15: Validar Código de Cupom (Parceiro)**

* Como um **Parceiro**,
* Eu quero **informar o código do cupom e visualizar seu status (válido, expirado ou utilizado)**,
* Para que **eu confirme a troca no atendimento**.

### Épico 5: Segurança, Autenticação e Conformidade

**US16: Ativar Conta a partir de Pré-Cadastro (Professor)**

* Como um **Professor**,
* Eu quero **ativar minha conta a partir do pré-cadastro recebido, definindo minha senha**,
* Para que **eu possa acessar o sistema e distribuir moedas**.

**US17: Recuperar Acesso (Esqueci Minha Senha)**

* Como um **Usuário**,
* Eu quero **recuperar minha senha por e-mail**,
* Para que **eu volte a acessar o sistema com segurança**.

**US18: Impedir Transações sem Saldo**

* Como o **Sistema**,
* Eu quero **bloquear envios e resgates quando o saldo for insuficiente**,
* Para que **a contabilidade de moedas permaneça consistente**.

**US19: Restringir Escopo por Instituição**

* Como o **Sistema**,
* Eu quero **restringir que professores encontrem e premiem apenas alunos da própria instituição**,
* Para que **se evitem operações cruzadas indevidas**.

**US20: Proteger Dados Pessoais (LGPD)**

* Como o **Sistema**,
* Eu quero **armazenar e transmitir dados de forma segura e aplicar máscaras a documentos**,
* Para que **o tratamento esteja em conformidade com a LGPD**.

---

# Diagrama de Casos de Uso
<img width="875" height="1937" alt="CasosUso drawio" src="https://github.com/user-attachments/assets/c24627f0-ead9-4036-802e-a334fcc87e7e" />

---

# Diagrama de Classes

<img width="850" height="1942" alt="diagrama_classes" src="https://github.com/user-attachments/assets/916c30ad-f735-464d-8a11-db25c9371f23" />


---

# Diagrama de Componentes

<img width="850" height="1940" alt="diagrama_componentes drawio" src="https://github.com/user-attachments/assets/06efbaee-2ca4-4512-a655-4803618dc1fe" />


---

# Diagrama ER

[imagem]

---

# Diagrama de Implantação

[imagem]


