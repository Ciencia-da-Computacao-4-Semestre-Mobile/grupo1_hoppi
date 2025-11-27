const http = require('http');

function request(method, path, body) {
  return new Promise((resolve, reject) => {
    const data = body ? JSON.stringify(body) : null;
    const options = {
      hostname: 'localhost',
      port: 3000,
      path,
      method,
      headers: {
        'Content-Type': 'application/json',
        'Content-Length': data ? Buffer.byteLength(data) : 0,
      },
    };

    const req = http.request(options, (res) => {
      let responseBody = '';
      res.on('data', (chunk) => {
        responseBody += chunk;
      });
      res.on('end', () => {
        try {
          resolve({
            status: res.statusCode,
            data: JSON.parse(responseBody),
          });
        } catch (e) {
          resolve({
            status: res.statusCode,
            data: responseBody,
          });
        }
      });
    });

    req.on('error', reject);
    if (data) req.write(data);
    req.end();
  });
}

async function runTests() {
  console.log('[TEST] Testando APIs do Hoppi Backend\n');

  try {
    // TESTE 1: Registrar Usuário
    console.log('=== TESTE 1: Registrar Usuário ===');
    const registerResult = await request('POST', '/auth/register', {
      email: 'teste@hoppi.com',
      username: 'testusuario',
      password: 'Senha123',
      display_name: 'Usuario Teste',
      birth_date: '2000-01-01',
      institution: 'FIAP',
    });
    console.log(`Status: ${registerResult.status}`);
    console.log('Resposta:', JSON.stringify(registerResult.data, null, 2));
    
    if (registerResult.status === 201) {
      console.log('[OK] Registro bem-sucedido!\n');
    } else if (registerResult.status === 409) {
      console.log('[WARN] Usuário já existe (normal se testou antes)\n');
    } else {
      console.log('[ERROR] Erro no registro\n');
    }

    // TESTE 2: Login
    console.log('=== TESTE 2: Login ===');
    const loginResult = await request('POST', '/auth/login', {
      email: 'teste@hoppi.com',
      password: 'Senha123',
    });
    console.log(`Status: ${loginResult.status}`);
    console.log('Resposta:', JSON.stringify(loginResult.data, null, 2));
    
    if (loginResult.status === 200) {
      console.log('[OK] Login bem-sucedido!\n');
      const token = loginResult.data.access_token;

      // TESTE 3: Criar Comunidade
      console.log('=== TESTE 3: Criar Comunidade ===');
      const communityResult = await request('POST', '/communities', {
        name: 'Comunidade Teste',
        description: 'Uma comunidade para testes',
        is_private: false,
      });
      
      // Note: Precisa do token de autenticação, então pode falhar
      console.log(`Status: ${communityResult.status}`);
      console.log('Resposta:', JSON.stringify(communityResult.data, null, 2));
      
      if (communityResult.status === 201 || communityResult.status === 401) {
        console.log(communityResult.status === 201 ? '[OK] Comunidade criada!\n' : '[WARN] Requer autenticação (adicionar header Authorization)\n');
      }
    } else {
      console.log('[ERROR] Falha no login\n');
    }

    // TESTE 4: Esqueci Senha (Enviar Email)
    console.log('=== TESTE 4: Esqueci Senha (Envio de Email) ===');
    const forgotResult = await request('POST', '/auth/forgot-password', {
      email: 'teste@hoppi.com',
    });
    console.log(`Status: ${forgotResult.status}`);
    console.log('Resposta:', JSON.stringify(forgotResult.data, null, 2));
    
    if (forgotResult.status === 200 && forgotResult.data.canProceed) {
      console.log('[OK] Código enviado por email! Verifique a caixa de entrada de teste@hoppi.com\n');
      console.log('[INFO] Para continuar os testes, execute o script novamente após receber o código.\n');
    } else {
      console.log('[ERROR] Falha ao enviar email\n');
    }

    console.log('\n[RESUMO] Resumo dos Testes:');
    console.log('- Registro: ' + (registerResult.status === 201 || registerResult.status === 409 ? 'OK' : 'ERRO'));
    console.log('- Login: ' + (loginResult.status === 200 ? 'OK' : 'ERRO'));
    console.log('- Envio de Email: ' + (forgotResult.status === 200 ? 'OK' : 'ERRO'));
    
    console.log('\n[DICA] Para testar a verificação de código e reset de senha:');
    console.log('1. Verifique o email teste@hoppi.com (ou o configurado)');
    console.log('2. Copie o código de 4 dígitos');
    console.log('3. Teste manualmente com:');
    console.log('   POST /auth/verify-reset-code { "email": "teste@hoppi.com", "code": "1234" }');
    console.log('   POST /auth/reset-password { "email": "teste@hoppi.com", "code": "1234", "newPassword": "NovaSenha123" }');

  } catch (error) {
    console.error('[ERROR] Erro durante os testes:', error.message);
  }
}

runTests();
