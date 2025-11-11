# 🚀 Sistema de Moeda Estudantil - Guia Completo de Instalação e Execução

## 📋 Índice

1. [Resumo Rápido - Sequência de Comandos](#resumo-rápido---sequência-de-comandos)
2. [Pré-requisitos](#pré-requisitos)
3. [Instalação do MySQL](#instalação-do-mysql)
4. [Configuração do Banco de Dados](#configuração-do-banco-de-dados)
5. [Configuração do Back-end](#configuração-do-back-end)
6. [Executando o Back-end](#executando-o-back-end)
7. [Executando o Front-end](#executando-o-front-end)
8. [Testando a Aplicação](#testando-a-aplicação)
9. [Troubleshooting](#troubleshooting)

---

## ⚡ Resumo Rápido - Sequência de Comandos

**Se você já tem tudo instalado, aqui está a sequência completa de comandos:**

### 1. Iniciar MySQL

**Windows (PowerShell como Administrador):**
```bash
net start MySQL80
```

**Linux:**
```bash
sudo systemctl start mysql
```

**macOS:**
```bash
brew services start mysql
```

### 2. Criar Banco de Dados

```bash
# Conectar ao MySQL
mysql -u root -p
# (digite sua senha)

# Dentro do MySQL:
CREATE DATABASE IF NOT EXISTS moedaestudantil CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
SHOW DATABASES;
EXIT;
```

### 3. Configurar Back-end

```bash
# Navegar até a pasta back
cd back

# Editar application.properties e configurar:
# spring.datasource.username=root
# spring.datasource.password=sua_senha
```

### 4. Executar Back-end

```bash
# Na pasta back
cd back

# Executar
mvn spring-boot:run

# Aguarde: "Started MoedaEstudantilApplication"
```

### 5. Executar Front-end (em outro terminal)

```bash
# Navegar até a pasta front
cd front

# Opção 1: Abrir diretamente
start index.html        # Windows
xdg-open index.html     # Linux
open index.html         # Mac

# Opção 2: Servidor local
python -m http.server 8000
```

### 6. Verificar no MySQL

```bash
# Conectar ao MySQL
mysql -u root -p

# Dentro do MySQL:
USE moedaestudantil;
SHOW TABLES;
SELECT * FROM alunos;
EXIT;
```

**Para detalhes completos, continue lendo o guia abaixo.**

---

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- ✅ **Java 21** ou superior
- ✅ **Maven 3.6+**
- ✅ **MySQL 8.0+** (ou MariaDB 10.5+)
- ✅ **Navegador web moderno** (Chrome, Firefox, Edge)
- ✅ **Git** (opcional, para clonar o repositório)

### Verificando Instalações

```bash
# Verificar Java
java -version
# Deve mostrar: openjdk version "21" ou superior

# Verificar Maven
mvn -version
# Deve mostrar: Apache Maven 3.6.x ou superior

# Verificar MySQL
mysql --version
# Deve mostrar: mysql Ver 8.0.x ou superior
```

---

## 🗄️ Instalação do MySQL

### Windows

1. **Baixar MySQL:**
   - Acesse: https://dev.mysql.com/downloads/installer/
   - Baixe o MySQL Installer (Windows)
   - Execute o instalador

2. **Durante a Instalação:**
   - Escolha "Developer Default" ou "Server only"
   - Configure a senha do usuário `root`
   - **ANOTE A SENHA** - você precisará dela depois
   - Mantenha a porta padrão: `3306`

3. **Verificar Instalação:**
   ```bash
   # Abra o MySQL Command Line Client ou PowerShell
   mysql -u root -p
   # Digite a senha quando solicitado
   ```

### Linux (Ubuntu/Debian)

```bash
# Atualizar pacotes
sudo apt update

# Instalar MySQL
sudo apt install mysql-server

# Configurar MySQL
sudo mysql_secure_installation

# Acessar MySQL
sudo mysql -u root -p
```

### macOS

```bash
# Usando Homebrew
brew install mysql

# Iniciar MySQL
brew services start mysql

# Configurar senha
mysql_secure_installation

# Acessar MySQL
mysql -u root -p
```

---

## ⚙️ Configuração do Banco de Dados

### Passo 1: Iniciar o Serviço MySQL

**IMPORTANTE:** O MySQL precisa estar rodando antes de iniciar o back-end.

**Windows:**
```bash
# Opção 1: Via linha de comando (PowerShell como Administrador)
net start MySQL80

# Opção 2: Via Services
# Pressione Win + R, digite: services.msc
# Procure por "MySQL80" e clique em "Iniciar"

# Opção 3: MySQL Workbench inicia automaticamente

# Verificar se está rodando:
sc query MySQL80
```

**Linux:**
```bash
# Iniciar MySQL
sudo systemctl start mysql

# Verificar status
sudo systemctl status mysql
# Deve mostrar: "Active: active (running)"

# Habilitar para iniciar automaticamente ao boot
sudo systemctl enable mysql
```

**macOS:**
```bash
# Iniciar MySQL
brew services start mysql

# Verificar status
brew services list
# Deve mostrar: mysql started
```

### Passo 2: Conectar ao MySQL e Criar o Banco

Abra o terminal e execute:

```bash
# Conectar ao MySQL (será solicitada a senha)
mysql -u root -p
```

**Digite sua senha quando solicitado.**

Depois, dentro do MySQL, execute:

```sql
-- Criar o banco de dados
CREATE DATABASE IF NOT EXISTS moedaestudantil CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Verificar se foi criado
SHOW DATABASES;

-- Você deve ver 'moedaestudantil' na lista

-- Sair do MySQL
EXIT;
```

**Sequência completa no terminal:**
```bash
# 1. Conectar ao MySQL
mysql -u root -p
# (digite a senha)

# 2. Dentro do MySQL, criar o banco:
CREATE DATABASE IF NOT EXISTS moedaestudantil CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 3. Verificar
SHOW DATABASES;

# 4. Sair
EXIT;
```

**Nota:** Se você não criar manualmente, o Spring Boot criará automaticamente quando a aplicação iniciar (graças à configuração `createDatabaseIfNotExist=true`). Mas é recomendado criar manualmente para garantir.

### Passo 3: Verificar Credenciais e Configuração

Anote suas credenciais MySQL:
- **Usuário:** `root` (ou outro usuário que você criou)
- **Senha:** (a senha que você configurou durante a instalação)
- **Porta:** `3306` (padrão)
- **Host:** `localhost`

**Testar conexão:**
```bash
# Testar se consegue conectar
mysql -u root -p -e "SELECT VERSION();"
# Se funcionar, você verá a versão do MySQL
```

---

## 🔧 Configuração do Back-end

### Passo 1: Navegar até a Pasta do Back-end

```bash
cd back
```

### Passo 2: Configurar application.properties

Abra o arquivo: `src/main/resources/application.properties`

Edite as seguintes linhas com suas credenciais MySQL:

```properties
# Configuração do MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/moedaestudantil?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root          # ← ALTERE AQUI: seu usuário MySQL
spring.datasource.password=              # ← ALTERE AQUI: sua senha MySQL
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

**Exemplo:**
```properties
spring.datasource.username=root
spring.datasource.password=minhasenha123
```

### Passo 3: Verificar Dependências

O arquivo `pom.xml` já está configurado com:
- ✅ Spring Boot 3.3.3
- ✅ MySQL Connector
- ✅ JPA/Hibernate
- ✅ Spring Security
- ✅ Validação

**Não é necessário alterar nada no `pom.xml`.**

---

## 🚀 Executando o Back-end

### Passo 1: Navegar até a Pasta do Back-end

Abra o terminal e execute:

```bash
# Navegar até a pasta do projeto
cd caminho/para/moedaestudantil/back

# Exemplo no Windows:
cd C:\Users\jvito\Downloads\lab-sistema-moeda-estudantil-main\lab-sistema-moeda-estudantil-main\code\moedaestudantil\back

# Exemplo no Linux/Mac:
cd ~/Downloads/lab-sistema-moeda-estudantil-main/lab-sistema-moeda-estudantil-main/code/moedaestudantil/back
```

### Passo 2: Verificar se o MySQL Está Rodando

**Antes de iniciar o back-end, certifique-se que o MySQL está rodando:**

**Windows:**
```bash
# Verificar se MySQL está rodando
sc query MySQL80
# Se não estiver, inicie:
net start MySQL80
```

**Linux:**
```bash
# Verificar status
sudo systemctl status mysql
# Se não estiver rodando, inicie:
sudo systemctl start mysql
```

**macOS:**
```bash
# Verificar status
brew services list | grep mysql
# Se não estiver rodando, inicie:
brew services start mysql
```

### Passo 3: Executar o Back-end

**Opção 1: Usando Maven (Recomendado)**

```bash
# Certifique-se de estar na pasta 'back'
cd back

# Executar a aplicação
mvn spring-boot:run
```

**Sequência completa de comandos no terminal:**
```bash
# 1. Navegar até a pasta back
cd back

# 2. Executar (Maven baixará dependências na primeira vez)
mvn spring-boot:run

# Aguarde até ver: "Started MoedaEstudantilApplication"
```

### Opção 2: Usando IDE (IntelliJ IDEA / Eclipse)

1. **Importar o Projeto:**
   - Abra sua IDE
   - File → Open → Selecione a pasta `back`
   - Aguarde o Maven baixar as dependências

2. **Executar:**
   - Localize a classe `MoedaEstudantilApplication.java`
   - Clique com botão direito → Run

### Verificando se o Back-end Está Rodando

Você deve ver mensagens como:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.3.3)

Started MoedaEstudantilApplication in X.XXX seconds
```

**URLs Disponíveis:**
- API Base: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/v3/api-docs`

### Testando o Back-end

Abra seu navegador e acesse:
```
http://localhost:8080/api/alunos
```

Você deve ver: `[]` (array vazio, pois ainda não há dados)

**Se aparecer erro de conexão MySQL:**
- Verifique se o MySQL está rodando
- Verifique as credenciais no `application.properties`
- Veja a seção [Troubleshooting](#troubleshooting)

---

## 🎨 Executando o Front-end

### Passo 1: Verificar se o Back-end Está Rodando

**IMPORTANTE:** O front-end precisa do back-end rodando na porta 8080.

Abra outro terminal e verifique:
```bash
# Verificar se a porta 8080 está em uso (back-end rodando)
# Windows:
netstat -an | findstr :8080

# Linux/Mac:
netstat -an | grep 8080
# ou
lsof -i :8080
```

Se não aparecer nada, o back-end não está rodando. Volte e inicie o back-end primeiro.

### Passo 2: Executar o Front-end

**Opção 1: Abrir Diretamente no Navegador (Mais Simples)**

1. **Navegue até a pasta front:**
   ```bash
   # A partir da raiz do projeto
   cd front
   ```

2. **Abra o arquivo `index.html`:**
   - **Windows:** 
     ```bash
     # No terminal:
     start index.html
     # Ou clique duas vezes no arquivo no explorador
     ```
   - **Linux:** 
     ```bash
     xdg-open index.html
     ```
   - **Mac:** 
     ```bash
     open index.html
     ```

3. **Ou arraste o arquivo para o navegador**

**Sequência completa:**
```bash
# 1. Navegar até a pasta front
cd front

# 2. Abrir no navegador
# Windows:
start index.html
# Linux:
xdg-open index.html
# Mac:
open index.html
```

### Opção 2: Usando Servidor Local (Recomendado para Desenvolvimento)

#### Python 3 (Mais Comum)

**Sequência completa de comandos:**
```bash
# 1. Navegar até a pasta front
cd front

# 2. Iniciar servidor
python -m http.server 8000

# Ou se tiver Python 2
python -m SimpleHTTPServer 8000

# 3. Você verá:
# Serving HTTP on 0.0.0.0 port 8000 (http://0.0.0.0:8000/) ...
```

**Acesse no navegador:** `http://localhost:8000`

**Para parar o servidor:** Pressione `Ctrl + C` no terminal

#### Node.js (http-server)

**Sequência completa de comandos:**
```bash
# 1. Instalar http-server globalmente (apenas uma vez)
npm install -g http-server

# 2. Navegar até a pasta front
cd front

# 3. Iniciar servidor
http-server -p 8000

# 4. Você verá:
# Starting up http-server, serving ./
# Available on: http://localhost:8000
```

**Acesse no navegador:** `http://localhost:8000`

**Para parar o servidor:** Pressione `Ctrl + C` no terminal

#### PHP (Alternativa)

**Sequência completa de comandos:**
```bash
# 1. Navegar até a pasta front
cd front

# 2. Iniciar servidor
php -S localhost:8000

# 3. Você verá:
# PHP 8.x.x Development Server (http://localhost:8000) started
```

**Acesse no navegador:** `http://localhost:8000`

**Para parar o servidor:** Pressione `Ctrl + C` no terminal

### Verificando o Front-end

1. Abra o navegador em `http://localhost:8000` (ou abra o `index.html` diretamente)
2. Você deve ver a página inicial com:
   - Título "Sistema de Moeda Estudantil"
   - Seções: Para Alunos, Para Professores, Para Empresas
   - Botões de navegação

**Se aparecer erro de CORS ou conexão:**
- Verifique se o back-end está rodando na porta 8080
- Abra o Console do navegador (F12) para ver erros
- Veja a seção [Troubleshooting](#troubleshooting)

---

## 🧪 Testando a Aplicação

### Pré-requisitos para Teste

Antes de começar, certifique-se de ter:
- ✅ MySQL rodando
- ✅ Back-end rodando na porta 8080
- ✅ Front-end aberto no navegador

### Fluxo Completo de Teste

#### 1. Cadastrar uma Instituição de Ensino (via Swagger ou API)

**Opção A: Via Swagger (Mais Fácil)**

1. **Acesse o Swagger no navegador:**
   ```
   http://localhost:8080/swagger-ui.html
   ```

2. **Encontre `InstituicaoEnsinoController`**

3. **Clique em `POST /api/instituicoes`**

4. **Clique em "Try it out"**

5. **Cole o JSON abaixo no campo "Request body":**
   ```json
   {
     "nome": "Universidade Federal de Tecnologia",
     "cnpj": "12345678000190",
     "endereco": "Rua Exemplo, 123",
     "telefone": "(11) 1234-5678",
     "email": "contato@universidade.edu.br"
   }
   ```

6. **Clique em "Execute"**

7. **Verifique a resposta (deve ser 201 Created)**

**Opção B: Via Terminal (cURL)**

**Usando Swagger:**
1. Acesse: `http://localhost:8080/swagger-ui.html`
2. Encontre `InstituicaoEnsinoController`
3. Use o endpoint `POST /api/instituicoes`
4. Body exemplo:
```json
{
  "nome": "Universidade Federal de Tecnologia",
  "cnpj": "12345678000190",
  "endereco": "Rua Exemplo, 123",
  "telefone": "(11) 1234-5678",
  "email": "contato@universidade.edu.br"
}
```

**Sequência completa de comandos no terminal:**
```bash
# No Windows (PowerShell ou Git Bash):
curl -X POST http://localhost:8080/api/instituicoes -H "Content-Type: application/json" -d "{\"nome\":\"Universidade Federal de Tecnologia\",\"cnpj\":\"12345678000190\",\"endereco\":\"Rua Exemplo, 123\",\"telefone\":\"(11) 1234-5678\",\"email\":\"contato@universidade.edu.br\"}"

# No Linux/Mac:
curl -X POST http://localhost:8080/api/instituicoes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Universidade Federal de Tecnologia",
    "cnpj": "12345678000190",
    "endereco": "Rua Exemplo, 123",
    "telefone": "(11) 1234-5678",
    "email": "contato@universidade.edu.br"
  }'
```

**Resposta esperada:**
```json
{
  "id": 1,
  "nome": "Universidade Federal de Tecnologia",
  "cnpj": "12345678000190",
  ...
}
```

#### 2. Cadastrar um Aluno (via Front-end)

1. No front-end, clique em **"Cadastrar Aluno"**
2. Preencha o formulário:
   - Nome: João Silva
   - Email: joao@email.com
   - CPF: 123.456.789-00
   - RG: 1234567
   - Endereço: Rua A, 100
   - Instituição: Selecione a instituição criada
   - Curso: Engenharia de Software
   - Senha: 123456
3. Clique em **"Cadastrar Aluno"**
4. Você deve ver mensagem de sucesso

#### 3. Cadastrar uma Empresa (via Front-end)

1. No front-end, clique em **"Cadastrar Empresa"**
2. Preencha o formulário:
   - Nome: Restaurante Universitário
   - CNPJ: 12.345.678/0001-90
   - Email: contato@restaurante.com
   - Senha: 123456
3. Clique em **"Cadastrar Empresa"**
4. Você será redirecionado para cadastro de vantagem

#### 4. Cadastrar uma Vantagem (via Front-end)

1. No front-end, clique em **"Cadastrar Vantagem"**
2. Preencha o formulário:
   - Empresa: Selecione a empresa criada
   - Descrição: Desconto de 20% em refeições
   - Custo: 50.00
   - Foto URL: (opcional) https://exemplo.com/foto.jpg
3. Clique em **"Cadastrar Vantagem"**
4. Você será redirecionado para listagem de vantagens

#### 5. Visualizar Vantagens (via Front-end)

1. No front-end, clique em **"Ver Vantagens"**
2. Você deve ver a vantagem cadastrada em um card
3. Informações exibidas: descrição, custo, empresa

#### 6. Resgatar uma Vantagem (via Front-end)

1. No front-end, clique em **"Resgatar Vantagem"**
2. Selecione:
   - Aluno: João Silva
   - Vantagem: Desconto de 20% em refeições
3. Clique em **"Resgatar Vantagem"**
4. O saldo do aluno será descontado
5. Um código de cupom será gerado

#### 7. Consultar Extrato (via Front-end)

1. No front-end, clique em **"Meu Extrato"** (Aluno ou Professor)
2. Selecione o aluno/professor
3. Clique em **"Consultar Extrato"**
4. Você verá:
   - Saldo atual
   - Histórico de transações

#### 8. Enviar Moedas (via Front-end)

1. No front-end, clique em **"Enviar Moedas"**
2. Selecione:
   - Professor: (precisa estar cadastrado via API/Swagger)
   - Aluno: João Silva
   - Quantidade: 10.00
   - Motivo: Excelente participação em aula
3. Clique em **"Enviar Moedas"**
4. O saldo do professor será debitado
5. O saldo do aluno será creditado

### Verificando no Banco de Dados

Para verificar se os dados estão sendo salvos no MySQL, execute no terminal:

**Sequência completa de comandos:**
```bash
# 1. Conectar ao MySQL
mysql -u root -p
# (digite a senha quando solicitado)

# 2. Dentro do MySQL, usar o banco
USE moedaestudantil;

# 3. Ver todas as tabelas criadas
SHOW TABLES;
# Você deve ver: alunos, empresas_parceiras, vantagens, transacoes, instituicoes_ensino, professores

# 4. Ver dados cadastrados (execute um por vez)
SELECT * FROM instituicoes_ensino;
SELECT * FROM alunos;
SELECT * FROM empresas_parceiras;
SELECT * FROM vantagens;
SELECT * FROM transacoes;
SELECT * FROM professores;

# 5. Contar registros
SELECT COUNT(*) as total_alunos FROM alunos;
SELECT COUNT(*) as total_empresas FROM empresas_parceiras;
SELECT COUNT(*) as total_vantagens FROM vantagens;

# 6. Sair do MySQL
EXIT;
```

**Exemplo de saída esperada:**
```
mysql> USE moedaestudantil;
Database changed

mysql> SHOW TABLES;
+-------------------------------+
| Tables_in_moedaestudantil     |
+-------------------------------+
| alunos                        |
| empresas_parceiras            |
| instituicoes_ensino           |
| professores                   |
| transacoes                    |
| vantagens                     |
+-------------------------------+
6 rows in set (0.00 sec)

mysql> SELECT * FROM alunos;
+----+-----------+------------------+...
| id | nome      | email            |...
+----+-----------+------------------+...
|  1 | João Silva| joao@email.com   |...
+----+-----------+------------------+...
```

---

## 🔍 Troubleshooting

### Problema: Erro de Conexão com MySQL

**Sintomas:**
```
com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure
```

**Soluções:**
1. Verifique se o MySQL está rodando:
   ```bash
   # Windows
   services.msc → Procure por MySQL
   
   # Linux
   sudo systemctl status mysql
   
   # Mac
   brew services list
   ```

2. Verifique as credenciais no `application.properties`

3. Teste a conexão manualmente:
   ```bash
   mysql -u root -p
   ```

4. Verifique se a porta 3306 está livre:
   ```bash
   # Windows
   netstat -an | findstr 3306
   
   # Linux/Mac
   netstat -an | grep 3306
   ```

### Problema: Erro "Access Denied"

**Sintomas:**
```
Access denied for user 'root'@'localhost'
```

**Soluções:**
1. Verifique o usuário e senha no `application.properties`
2. Teste a conexão:
   ```bash
   mysql -u root -p
   ```
3. Se necessário, crie um novo usuário:
   ```sql
   CREATE USER 'moedaestudantil'@'localhost' IDENTIFIED BY 'senha123';
   GRANT ALL PRIVILEGES ON moedaestudantil.* TO 'moedaestudantil'@'localhost';
   FLUSH PRIVILEGES;
   ```
4. Atualize o `application.properties` com o novo usuário

### Problema: Erro de CORS no Front-end

**Sintomas:**
```
Access to fetch at 'http://localhost:8080/api/...' from origin '...' has been blocked by CORS policy
```

**Soluções:**
1. Verifique se o back-end está rodando na porta 8080
2. O back-end já está configurado para permitir CORS
3. Se persistir, verifique o `SecurityConfig.java`

### Problema: Porta 8080 já está em uso

**Sintomas:**
```
Port 8080 is already in use
```

**Soluções:**
1. Encontre o processo usando a porta:
   ```bash
   # Windows
   netstat -ano | findstr :8080
   
   # Linux/Mac
   lsof -i :8080
   ```

2. Encerre o processo ou altere a porta no `application.properties`:
   ```properties
   server.port=8081
   ```

3. Atualize o `api.js` no front-end:
   ```javascript
   const API_BASE_URL = 'http://localhost:8081/api';
   ```

### Problema: Tabelas não são criadas

**Sintomas:**
```
Table 'moedaestudantil.alunos' doesn't exist
```

**Soluções:**
1. Verifique se `spring.jpa.hibernate.ddl-auto=update` está no `application.properties`
2. Verifique se o banco de dados existe:
   ```sql
   SHOW DATABASES;
   ```
3. Verifique os logs do Spring Boot para erros de criação de tabelas

### Problema: Front-end não carrega

**Sintomas:**
- Página em branco
- Erros no console do navegador

**Soluções:**
1. Abra o Console do navegador (F12)
2. Verifique erros JavaScript
3. Verifique se todos os arquivos estão na pasta `front`:
   - `index.html`
   - `api.js`
   - `styles.css`
   - Todos os arquivos `.html` e `.js`
4. Tente usar um servidor local em vez de abrir diretamente

### Problema: Dados não aparecem

**Sintomas:**
- Formulário envia, mas dados não aparecem

**Soluções:**
1. Verifique o console do navegador (F12) para erros
2. Verifique se o back-end está rodando
3. Verifique os logs do Spring Boot
4. Teste a API diretamente via Swagger ou cURL

---

## 📚 Estrutura Completa do Projeto

```
moedaestudantil/
├── back/                          # Back-end Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/universidade/moedaestudantil/
│   │   │   │       ├── config/    # Configurações (Security, Web)
│   │   │   │       ├── controller/ # Controllers REST
│   │   │   │       ├── model/      # Entidades JPA
│   │   │   │       ├── repository/ # Repositories JPA
│   │   │   │       ├── service/     # Services (lógica de negócio)
│   │   │   │       └── exception/  # Exception handlers
│   │   │   └── resources/
│   │   │       └── application.properties # Configurações
│   │   └── test/
│   ├── pom.xml                    # Dependências Maven
│   └── target/                    # Arquivos compilados
│
├── front/                         # Front-end HTML/CSS/JS
│   ├── index.html                 # Página inicial
│   ├── cadastro-aluno.html        # Cadastro de aluno
│   ├── cadastro-empresa.html      # Cadastro de empresa
│   ├── cadastro-vantagem.html     # Cadastro de vantagem
│   ├── listagem-vantagens.html    # Listagem de vantagens
│   ├── resgate-vantagem.html      # Resgate de vantagem
│   ├── envio-moedas.html          # Envio de moedas
│   ├── extrato-aluno.html         # Extrato de aluno
│   ├── extrato-professor.html     # Extrato de professor
│   ├── api.js                     # Funções de API
│   ├── styles.css                 # Estilos CSS
│   ├── *.js                       # Scripts JavaScript
│   └── README.md                  # Este arquivo
│
└── docs/                          # Documentação
    ├── Sistema de Moeda Estudantil.txt
    └── VERIFICACAO_FINAL.md
```

---

## 🔗 URLs Importantes

### Back-end
- **API Base:** `http://localhost:8080/api`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **API Docs (JSON):** `http://localhost:8080/v3/api-docs`

### Front-end
- **Página Inicial:** `http://localhost:8000/index.html` (ou abra diretamente)
- **Cadastro Aluno:** `http://localhost:8000/cadastro-aluno.html`
- **Cadastro Empresa:** `http://localhost:8000/cadastro-empresa.html`
- **Cadastro Vantagem:** `http://localhost:8000/cadastro-vantagem.html`
- **Listagem Vantagens:** `http://localhost:8000/listagem-vantagens.html`
- **Resgate Vantagem:** `http://localhost:8000/resgate-vantagem.html`
- **Envio Moedas:** `http://localhost:8000/envio-moedas.html`
- **Extrato Aluno:** `http://localhost:8000/extrato-aluno.html`
- **Extrato Professor:** `http://localhost:8000/extrato-professor.html`

### Banco de Dados
- **Host:** `localhost`
- **Porta:** `3306`
- **Banco:** `moedaestudantil`
- **Usuário:** `root` (ou configurado)
- **Senha:** (configurada)

---

## ✅ Checklist de Verificação

Antes de considerar tudo funcionando, verifique:

### Configuração Inicial
- [ ] MySQL está instalado
- [ ] MySQL está rodando (verificado com `systemctl status mysql` ou `sc query MySQL80`)
- [ ] Banco de dados `moedaestudantil` foi criado (ou será criado automaticamente)
- [ ] Credenciais MySQL estão corretas no `application.properties`
- [ ] Testei conexão MySQL: `mysql -u root -p` funciona

### Back-end
- [ ] Back-end inicia sem erros na porta 8080
- [ ] Vejo a mensagem "Started MoedaEstudantilApplication" no terminal
- [ ] Swagger UI está acessível em `http://localhost:8080/swagger-ui.html`
- [ ] API responde: `http://localhost:8080/api/alunos` retorna `[]`

### Front-end
- [ ] Front-end abre corretamente (direto ou via servidor local)
- [ ] Página inicial carrega sem erros
- [ ] Console do navegador (F12) não mostra erros de conexão

### Funcionalidades
- [ ] É possível cadastrar uma instituição (via Swagger)
- [ ] É possível cadastrar um aluno (via front-end)
- [ ] É possível cadastrar uma empresa (via front-end)
- [ ] É possível cadastrar uma vantagem (via front-end)
- [ ] É possível visualizar vantagens (via front-end)
- [ ] É possível resgatar uma vantagem (via front-end)
- [ ] É possível consultar extratos (via front-end)
- [ ] Dados aparecem no banco MySQL quando consultados com `SELECT * FROM alunos;`

---

## 🎓 Próximos Passos

Após ter tudo funcionando:

1. **Explorar o Swagger UI** para testar todos os endpoints
2. **Cadastrar dados de teste** para popular o sistema
3. **Testar todos os fluxos** de usuário
4. **Verificar os dados no MySQL** para entender a estrutura
5. **Personalizar** conforme suas necessidades

---

## 📞 Suporte

Se encontrar problemas:

1. Verifique a seção [Troubleshooting](#troubleshooting)
2. Verifique os logs do Spring Boot no console
3. Verifique o Console do navegador (F12)
4. Verifique os logs do MySQL
5. Consulte a documentação: `docs/VERIFICACAO_FINAL.md`

---

## 📝 Notas Finais

- **Desenvolvimento:** Este sistema foi desenvolvido para funcionar localmente
- **Produção:** Para produção, ajuste as configurações de segurança e CORS
- **Autenticação:** Sistema de login não implementado (funcionalidade futura)
- **Email:** Notificações por email não implementadas (funcionalidade futura)

---

**Boa sorte e bom desenvolvimento! 🚀**
