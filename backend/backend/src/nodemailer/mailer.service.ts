import { MailerService } from "@nestjs-modules/mailer";
import { Injectable } from "@nestjs/common";

@Injectable()
export class MailService {

    constructor(
        private readonly mailerService: MailerService
    ) {}

    async sendMail(to: string) {
        try {
            await this.mailerService.sendMail({
            to,
            subject: 'Reset Password',
            template: 'reset-password',
            context: {
                name: 'User',
            },
        });
        } catch (error) {
            console.error('Error sending email:', error);
        }
    }
    
}