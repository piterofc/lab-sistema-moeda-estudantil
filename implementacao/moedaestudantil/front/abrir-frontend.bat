@echo off
REM Script para abrir o frontend no navegador padrão
REM Execute: abrir-frontend.bat

echo ========================================
echo   Sistema de Moeda Estudantil - Frontend
echo ========================================
echo.

REM Verificar se está na pasta correta
if not exist "index.html" (
    echo ERRO: Execute este script na pasta 'front'
    echo Pasta atual: %CD%
    pause
    exit /b 1
)

echo Abrindo o frontend no navegador...
echo.
echo IMPORTANTE: Para evitar problemas de CORS, use um servidor HTTP local.
echo.
echo Opcoes:
echo 1. Python: python -m http.server 8000
echo 2. Node.js: http-server -p 8000
echo 3. VS Code: Extensao Live Server
echo.

REM Tentar abrir com Python se disponível
python --version >nul 2>&1
if not errorlevel 1 (
    echo Iniciando servidor HTTP com Python na porta 8000...
    echo Acesse: http://localhost:8000
    echo.
    echo Pressione Ctrl+C para parar o servidor
    echo.
    start http://localhost:8000
    python -m http.server 8000
) else (
    echo Python nao encontrado. Abrindo arquivo diretamente...
    echo NOTA: Pode haver problemas de CORS se abrir diretamente.
    echo.
    start index.html
)

pause

