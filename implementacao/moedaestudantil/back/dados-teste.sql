-- Script SQL para criar dados de teste
-- Execute: mysql -u root -p < dados-teste.sql
-- Ou copie e cole no MySQL Workbench / Command Line Client

USE moedaestudantil;

-- Limpar dados existentes (CUIDADO: apaga tudo!)
-- DELETE FROM transacoes;
-- DELETE FROM vantagens;
-- DELETE FROM alunos;
-- DELETE FROM professores;
-- DELETE FROM empresas_parceiras;
-- DELETE FROM instituicoes_ensino;

-- 1. Criar Instituição de Ensino
INSERT INTO instituicoes_ensino (nome, cnpj, endereco) 
VALUES ('Universidade Federal de Teste', '12345678000190', 'Rua Universitária, 1000')
ON DUPLICATE KEY UPDATE nome = nome;

-- Obter ID da instituição (ajuste se necessário)
SET @instituicao_id = LAST_INSERT_ID();
-- Se já existir, use:
-- SET @instituicao_id = 1;

-- 2. Criar Aluno
INSERT INTO alunos (nome, email, cpf, rg, endereco, curso, saldo, senha, instituicao_id) 
VALUES 
    ('Aluno Teste', 'aluno@teste.com', '12345678900', '1234567', 'Rua A, 123', 'Engenharia de Software', 0.0, 'senha123', @instituicao_id)
ON DUPLICATE KEY UPDATE nome = nome;

-- 3. Criar Professor
-- NOTA: Professores usam CPF para login, não email
INSERT INTO professores (nome, cpf, departamento, saldo, senha, instituicao_id) 
VALUES 
    ('Professor Teste', '11122233344', 'Ciência da Computação', 1000.0, 'senha123', @instituicao_id)
ON DUPLICATE KEY UPDATE nome = nome;

-- 4. Criar Empresa Parceira
INSERT INTO empresas_parceiras (nome, cnpj, email, senha) 
VALUES 
    ('Empresa Teste', '12345678000100', 'empresa@teste.com', 'senha123')
ON DUPLICATE KEY UPDATE nome = nome;

-- 5. Criar Vantagens (opcional - pode criar via frontend também)
INSERT INTO vantagens (nome, descricao, custo, empresa_id) 
VALUES 
    ('Vantagem Teste', 'Vantagem de teste para desenvolvimento', 50.0, (SELECT id FROM empresas_parceiras WHERE email = 'empresa@teste.com' LIMIT 1))
ON DUPLICATE KEY UPDATE nome = nome;

-- 6. Verificar dados criados
SELECT '=== INSTITUIÇÕES ===' AS '';
SELECT * FROM instituicoes_ensino;

SELECT '=== ALUNOS ===' AS '';
SELECT id, nome, email, cpf, curso, saldo FROM alunos;

SELECT '=== PROFESSORES ===' AS '';
SELECT id, nome, cpf, departamento, saldo FROM professores;

SELECT '=== EMPRESAS ===' AS '';
SELECT id, nome, cnpj, email FROM empresas_parceiras;

SELECT '=== VANTAGENS ===' AS '';
SELECT id, nome, descricao, custo FROM vantagens;

-- 7. Credenciais de Login para Teste
SELECT '=== CREDENCIAIS DE LOGIN ===' AS '';
SELECT 'ALUNO - Email: aluno@teste.com | Senha: senha123' AS '';
SELECT 'PROFESSOR - CPF: 11122233344 | Senha: senha123' AS '';
SELECT 'EMPRESA - Email: empresa@teste.com | Senha: senha123' AS '';

