import { Module } from '@nestjs/common';
import { AuthController } from './auth.controller';
import { AuthService } from './auth.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { User } from 'src/users/users.entity';
import { JwtModule } from '@nestjs/jwt';
import { PassportModule } from '@nestjs/passport';
import { MailModule } from 'src/nodemailer/mailer.module';

@Module({
    imports: [
        TypeOrmModule.forFeature([User]),
        PassportModule.register({ defaultStrategy: 'jwt' }),
        JwtModule.register({
            global: true,
            secret: "secret",
            signOptions: {
                expiresIn: '60s',
            }
        }),
        MailModule
    ],
    controllers: [AuthController],
    providers: [AuthService],
    exports: [AuthService, PassportModule],
})
export class AuthModule {}