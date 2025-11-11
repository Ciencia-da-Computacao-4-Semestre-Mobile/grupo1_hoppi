import { MailerModule } from "@nestjs-modules/mailer";
import { Module } from "@nestjs/common";
import { MailService } from "./mailer.service";

@Module({
    imports: [
        MailerModule.forRoot({
            transport: {
                host: 'send.smtp.mailtrap.io',
                port: 587,
                secure: false,
                auth: {
                    user: process.env.EMAIL_USER || 'example@example.com',
                    pass: process.env.EMAIL_PASSWORD || 'password'
                }
            },
            defaults: {
                from: '"No Reply" <no-reply@example.com>'
            }
        })
    ],
    providers: [MailService],
    exports: [MailService],
})
export class MailModule {}