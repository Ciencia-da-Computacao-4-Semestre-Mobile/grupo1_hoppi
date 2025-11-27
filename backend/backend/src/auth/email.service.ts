import { Injectable } from '@nestjs/common';
import * as nodemailer from 'nodemailer';

@Injectable()
export class EmailService {
  private transporter: nodemailer.Transporter;

  constructor() {
    this.transporter = nodemailer.createTransport({
      host: process.env.EMAIL_HOST || 'smtp.gmail.com',
      port: Number(process.env.EMAIL_PORT) || 587,
      secure: false,
      auth: {
        user: process.env.EMAIL_USER,
        pass: process.env.EMAIL_PASSWORD,
      },
    });
  }

  async sendPasswordResetCode(email: string, code: string): Promise<void> {
    const fromName = process.env.EMAIL_FROM_NAME || 'Hoppi - Não Responda';
    const mailOptions = {
      from: `${fromName} <${process.env.EMAIL_USER}>`,
      to: email,
      subject: 'Código de Recuperação - Hoppi',
      html: `
        <!DOCTYPE html>
        <html>
        <head>
          <style>
            body { 
              font-family: Arial, sans-serif; 
              background-color: #f4f4f4; 
              padding: 20px; 
              margin: 0;
            }
            .container { 
              max-width: 600px; 
              margin: 0 auto; 
              background: white; 
              padding: 30px; 
              border-radius: 10px; 
              box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }
            .header {
              text-align: center;
              margin-bottom: 30px;
            }
            .code { 
              font-size: 32px; 
              font-weight: bold; 
              color: #4CAF50; 
              letter-spacing: 8px; 
              text-align: center; 
              padding: 20px; 
              background: #f0f0f0; 
              border-radius: 5px; 
              margin: 20px 0;
            }
            .warning { 
              color: #ff6b6b; 
              font-size: 14px; 
              margin-top: 20px; 
              text-align: center;
            }
            .footer { 
              margin-top: 30px; 
              padding-top: 20px; 
              border-top: 1px solid #ddd; 
              color: #666; 
              font-size: 12px; 
              text-align: center;
            }
          </style>
        </head>
        <body>
          <div class="container">
            <div class="header">
              <h2>Recuperação de Senha</h2>
            </div>
            <p>Olá,</p>
            <p>Você solicitou a recuperação de senha no aplicativo <strong>Hoppi</strong>.</p>
            <p>Utilize o código abaixo para continuar:</p>
            
            <div class="code">${code}</div>
            
            <p class="warning">AVISO: Este código expira em <strong>10 minutos</strong>.</p>
            <p>Se você não solicitou esta recuperação, ignore este e-mail.</p>
            
            <div class="footer">
              <p>Este é um e-mail automático, por favor não responda.</p>
              <p><strong>Hoppi</strong> - Conectando Comunidades</p>
            </div>
          </div>
        </body>
        </html>
      `,
    };

    await this.transporter.sendMail(mailOptions);
  }
}
