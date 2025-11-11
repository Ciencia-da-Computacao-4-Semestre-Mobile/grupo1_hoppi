#!/bin/bash

echo "=== TESTE 1: Registrar Usuário ==="
curl -X POST http://localhost:3000/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@hoppi.com",
    "username": "testusuario",
    "password": "Senha123",
    "display_name": "Usuario Teste",
    "birth_date": "2000-01-01",
    "institution": "FIAP"
  }'
echo -e "\n\n"

echo "=== TESTE 2: Login ==="
curl -X POST http://localhost:3000/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@hoppi.com",
    "password": "Senha123"
  }'
echo -e "\n\n"

echo "=== TESTE 3: Esqueci Senha (Enviar Email) ==="
curl -X POST http://localhost:3000/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@hoppi.com"
  }'
echo -e "\n\n"

echo "=== Aguarde receber o código no email e insira aqui: ==="
read -p "Digite o código de 4 dígitos: " CODE

echo "=== TESTE 4: Verificar Código ==="
curl -X POST http://localhost:3000/auth/verify-reset-code \
  -H "Content-Type: application/json" \
  -d "{
    \"email\": \"teste@hoppi.com\",
    \"code\": \"$CODE\"
  }"
echo -e "\n\n"

echo "=== TESTE 5: Resetar Senha ==="
curl -X POST http://localhost:3000/auth/reset-password \
  -H "Content-Type: application/json" \
  -d "{
    \"email\": \"teste@hoppi.com\",
    \"code\": \"$CODE\",
    \"newPassword\": \"NovaSenha123\"
  }"
echo -e "\n\n"

echo "=== TESTE 6: Login com Nova Senha ==="
curl -X POST http://localhost:3000/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@hoppi.com",
    "password": "NovaSenha123"
  }'
echo -e "\n\n"
