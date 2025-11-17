# Script PowerShell para executar o backend
# Execute: .\executar-backend.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Sistema de Moeda Estudantil - Backend" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se está na pasta correta
if (-not (Test-Path "pom.xml")) {
    Write-Host "ERRO: Execute este script na pasta 'back'" -ForegroundColor Red
    Write-Host "Pasta atual: $(Get-Location)" -ForegroundColor Yellow
    exit 1
}

# Verificar Java
Write-Host "Verificando Java..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-Object -First 1
    Write-Host "Java encontrado: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "ERRO: Java não encontrado!" -ForegroundColor Red
    Write-Host "Instale o Java 21 e tente novamente." -ForegroundColor Yellow
    exit 1
}

# Verificar MySQL (tentativa de conexão)
Write-Host ""
Write-Host "Verificando MySQL..." -ForegroundColor Yellow
Write-Host "Certifique-se de que o MySQL está rodando na porta 3306" -ForegroundColor Yellow
Write-Host ""

# Compilar e executar
Write-Host "Compilando e executando o backend..." -ForegroundColor Yellow
Write-Host "Aguarde... isso pode levar alguns minutos na primeira vez." -ForegroundColor Yellow
Write-Host ""

.\mvnw.cmd clean spring-boot:run

