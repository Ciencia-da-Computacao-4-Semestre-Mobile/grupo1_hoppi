import { MailerModule, MailerService } from "@nestjs-modules/mailer";
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
                    user: 'suporte@hoppiiiiii.com',
                    pass: '8dbf6177b040352bad975cd8c4982780'
                }
            },
            defaults: {
                from: '"No Reply" <suporte@hoppiiiiii.com>'
            }
        })
    ],
    providers: [MailService],
    exports: [MailService],
})
export class MailModule {}