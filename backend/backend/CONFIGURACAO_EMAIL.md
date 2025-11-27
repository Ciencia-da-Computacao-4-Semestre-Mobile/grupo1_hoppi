# Configuração de Email para Recuperação de Senha

##  Como funciona?

A funcionalidade de recuperação de senha está **100% implementada** no código, mas precisa de configuração para funcionar em produção.

##  Configuração Necessária

### 1. Configure as variáveis de ambiente

Copie o arquivo `.env.example` para `.env`:
```bash
cp .env.example .env
```

Adicione as seguintes variáveis no seu arquivo `.env`:

```bash
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USER=seu-email@gmail.com
EMAIL_PASSWORD=sua-senha-de-app
```

### 2. Como obter senha de app do Gmail

1. Acesse sua [Conta Google](https://myaccount.google.com/)
2. Vá em **Segurança**
3. Ative a **Verificação em duas etapas** (se ainda não tiver)
4. Procure por **Senhas de app**
5. Selecione **E-mail** e **Outro (nome personalizado)**
6. Digite "Hoppi Backend"
7. Copie a senha gerada (16 caracteres)
8. Cole no `.env` em `EMAIL_PASSWORD`

** NUNCA use sua senha normal do Gmail!**

### 3. Teste em desenvolvimento

Você pode usar o **Mailtrap** ou **Ethereal** para testes sem enviar emails reais:

#### Opção 1: Mailtrap (Recomendado para testes)
```bash
EMAIL_HOST=smtp.mailtrap.io
EMAIL_PORT=2525
EMAIL_USER=seu-usuario-mailtrap
EMAIL_PASSWORD=sua-senha-mailtrap
```

Crie uma conta grátis em: https://mailtrap.io

#### Opção 2: Gmail (Produção)
```bash
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USER=noreply.hoppi@gmail.com
EMAIL_PASSWORD=xxxx-xxxx-xxxx-xxxx
```

### 4. Reinicie o servidor

Após configurar o `.env`, reinicie o backend:
```bash
npm run start:dev
```

##  Como testar

### 1. Solicitar código de recuperação
```bash
POST http://localhost:3000/auth/forgot-password
Content-Type: application/json

{
  "email": "usuario@example.com"
}
```

**Resposta esperada:**
```json
{
  "message": "Código enviado com sucesso!",
  "canProceed": true
}
```

### 2. Verificar o código (4 dígitos)
```bash
POST http://localhost:3000/auth/verify-reset-code
Content-Type: application/json

{
  "email": "usuario@example.com",
  "code": "1234"
}
```

**Resposta esperada:**
```json
{
  "valid": true,
  "message": "Código validado com sucesso!"
}
```

### 3. Resetar a senha
```bash
POST http://localhost:3000/auth/reset-password
Content-Type: application/json

{
  "email": "usuario@example.com",
  "code": "1234",
  "newPassword": "NovaSenha123"
}
```

**Requisitos da senha:**
- Mínimo 8 caracteres
- Pelo menos 1 letra maiúscula
- Pelo menos 1 número

**Resposta esperada:**
```json
{
  "message": "Senha alterada com sucesso!"
}
```

##  Para produção

### Recomendações:

1. **Use um serviço profissional de email:**
   - **SendGrid** (100 emails/dia grátis) - RECOMENDADO
   - **Mailgun** (5.000 emails/mês grátis)
   - **AWS SES** (62.000 emails/mês grátis)

2. **Configure um email "noreply":**
   - `noreply@hoppi.com.br`
   - `no-reply@hoppi.app`

3. **SendGrid (Recomendado):**
```bash
EMAIL_HOST=smtp.sendgrid.net
EMAIL_PORT=587
EMAIL_USER=apikey
EMAIL_PASSWORD=SG.sua-api-key-aqui
```

Crie conta grátis em: https://sendgrid.com

##  Fluxo completo

1. **Usuário esquece senha** → Insere email
2. **Backend gera código** → 4 dígitos aleatórios
3. **Backend salva no banco** → Tabela `password_resets` com expiração de 10 minutos
4. **Email é enviado** → Com código de 4 dígitos
5. **Usuário insere código** → Validação no backend
6. **Código válido** → Permite criar nova senha
7. **Nova senha definida** → Código marcado como usado

##  Status Atual

-  Código implementado e funcional
-  Tabela `password_resets` registrada no TypeORM
-  Validações de senha (8 chars, maiúscula, número)
-  Email com template HTML profissional
-  Expiração de código (10 minutos)
-  Proteção contra reutilização de código
-  **FALTA**: Configurar credenciais de email no `.env`

##  Segurança

- Códigos expiram em 10 minutos
- Códigos são marcados como "usados" após reset
- Não revela se email existe ou não (segurança)
- Senha hasheada com bcrypt antes de salvar
- Validação rigorosa de formato de senha
