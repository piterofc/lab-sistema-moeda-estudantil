@echo off
REM Script Batch para executar o backend no Windows
REM Execute: executar-backend.bat

echo ========================================
echo   Sistema de Moeda Estudantil - Backend
echo ========================================
echo.

REM Verificar se está na pasta correta
if not exist "pom.xml" (
    echo ERRO: Execute este script na pasta 'back'
    echo Pasta atual: %CD%
    pause
    exit /b 1
)

REM Verificar Java
echo Verificando Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERRO: Java nao encontrado!
    echo Instale o Java 21 e tente novamente.
    pause
    exit /b 1
)

echo.
echo Verificando MySQL...
echo Certifique-se de que o MySQL esta rodando na porta 3306
echo.

REM Compilar e executar
echo Compilando e executando o backend...
echo Aguarde... isso pode levar alguns minutos na primeira vez.
echo.

call mvnw.cmd clean spring-boot:run

pause

