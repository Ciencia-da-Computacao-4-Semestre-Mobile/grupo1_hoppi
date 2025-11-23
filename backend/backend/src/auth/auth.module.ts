import { Module } from '@nestjs/common';
import { AuthController } from './auth.controller';
import { AuthService } from './auth.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { User } from 'src/users/users.entity';
import { JwtModule } from '@nestjs/jwt';
import { PassportModule } from '@nestjs/passport';
import { MailModule } from 'src/nodemailer/mailer.module';
import { PasswordReset } from './entities/password-reset.entity';
import { EmailService } from './email.service';
import { env } from 'src/config/env';

@Module({
    imports: [
        TypeOrmModule.forFeature([User, PasswordReset]),
        PassportModule.register({ defaultStrategy: 'jwt' }),
        JwtModule.register({
            global: true,
            secret: env.JWT_SECRET,
            signOptions: {
                expiresIn: env.JWT_EXPIRES_IN,
            }
        }),
        MailModule
    ],
    controllers: [AuthController],
    providers: [AuthService, EmailService],
    exports: [AuthService, PassportModule],
})
export class AuthModule {}