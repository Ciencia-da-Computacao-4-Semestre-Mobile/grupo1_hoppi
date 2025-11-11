require('dotenv').config();
const nodemailer = require('nodemailer');

async function testEmail() {
  console.log('[TEST] Testando configuração de email...\n');
  
  console.log('Configurações:');
  console.log('  HOST:', process.env.EMAIL_HOST);
  console.log('  PORT:', process.env.EMAIL_PORT);
  console.log('  USER:', process.env.EMAIL_USER);
  console.log('  PASS:', process.env.EMAIL_PASSWORD ? '***' + process.env.EMAIL_PASSWORD.slice(-4) : 'NÃO CONFIGURADA');
  console.log();

  const transporter = nodemailer.createTransport({
    host: process.env.EMAIL_HOST,
    port: Number(process.env.EMAIL_PORT),
    secure: false,
    auth: {
      user: process.env.EMAIL_USER,
      pass: process.env.EMAIL_PASSWORD,
    },
  });

  try {
    console.log('[SMTP] Verificando conexão SMTP...');
    await transporter.verify();
    console.log('[OK] Conexão SMTP OK!\n');

    console.log('[EMAIL] Enviando email de teste...');
    const info = await transporter.sendMail({
      from: `"Hoppi - Não Responda" <${process.env.EMAIL_USER}>`,
      to: process.env.EMAIL_USER, // Envia para o próprio email
      subject: 'Teste - Código de Recuperação Hoppi',
      html: `
        <h2>Recuperação de Senha</h2>
        <p>Este é um email de teste do sistema de recuperação de senha.</p>
        <div style="font-size: 32px; font-weight: bold; color: #4CAF50; letter-spacing: 8px; text-align: center; padding: 20px; background: #f0f0f0; border-radius: 5px; margin: 20px 0;">
          1234
        </div>
        <p style="color: #ff6b6b;">AVISO: Este código expira em <strong>10 minutos</strong>.</p>
        <p><strong>Hoppi</strong> - Teste de configuração bem-sucedido!</p>
      `,
    });

    console.log('[OK] Email enviado com sucesso!');
    console.log('[INFO] MessageID:', info.messageId);
    console.log('\n[SUCCESS] Configuração funcionando perfeitamente!');
    console.log('[INFO] Verifique a caixa de entrada de:', process.env.EMAIL_USER);
    
  } catch (error) {
    console.error('[ERROR] Erro:', error.message);
    if (error.code === 'EAUTH') {
      console.log('\n[DICA] Verifique se:');
      console.log('  1. A verificação em 2 etapas está ativada');
      console.log('  2. A senha é uma "Senha de App" válida (16 caracteres)');
      console.log('  3. O email está correto');
    }
  }
  
  process.exit(0);
}

testEmail();
