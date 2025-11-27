import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { User } from "src/users/users.entity";
import { Repository } from "typeorm";
import * as bcrypt from 'bcrypt';
import * as crypto from 'crypto';
import { UnauthorizedException, ConflictException } from '@nestjs/common';
import { RegisterDTO, AuthLoginDTO } from "./schemas/auth.schema";
// import { MailService } from "src/nodemailer/mailer.service";
import { PasswordReset } from "./entities/password-reset.entity";
import { EmailService } from "./email.service";
import { JwtPayload } from "./interfaces/auth-request.interface";
import { JwtService } from "@nestjs/jwt";

@Injectable()
export class AuthService {
    constructor(
        @InjectRepository(User) private readonly userRepository: Repository<User>,
        @InjectRepository(PasswordReset) private readonly passwordResetRepository: Repository<PasswordReset>,
        private readonly jwtService: JwtService,


        private readonly emailService: EmailService
    ) {}

    async login( dto: AuthLoginDTO ) {
        const user = await this.userRepository.findOneBy({ email: dto.email });

        if (!user) {
            throw new UnauthorizedException('Invalid credentials');
        }
        // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment, @typescript-eslint/no-unsafe-call, @typescript-eslint/no-unsafe-member-access
        const passwordOk = await bcrypt.compare(dto.password, user.password_hash);
        if (!passwordOk) {
            throw new UnauthorizedException('Invalid credentials');
        }

         
        const token = this.createToken(user);

        return {
            access_token: token,
            user: this.sanitizeUser(user)
        };
    }

    async register(dto: RegisterDTO) {
        const userExists = await this.userRepository.findOneBy({ 
            email: dto.email 
        });

        if (userExists) {
            throw new ConflictException('Email already registered');
        }

        const usernameExists = await this.userRepository.findOneBy({ 
            username: dto.username 
        });

        if (usernameExists) {
            throw new ConflictException('Username already taken');
        }

        // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment, @typescript-eslint/no-unsafe-call, @typescript-eslint/no-unsafe-member-access
        const hashedPassword = await bcrypt.hash(dto.password, 10);

         
        const newUser = this.userRepository.create({
            email: dto.email,
            // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
            password_hash: hashedPassword,
            username: dto.username,
            display_name: dto.display_name,
            birth_date: dto.birth_date,
            institution: dto.institution
        });

        const savedUser = await this.userRepository.save(newUser);
         
        const token = this.createToken(savedUser);

        return {
            message: "Registration successful",
            access_token: token,
            user: this.sanitizeUser(savedUser)
        };
    }

    // Método legado não utilizado removido para limpar lint

    // NOVAS FUNCIONALIDADES DE RECUPERAÇÃO DE SENHA
    
    async forgotPassword(email: string): Promise<{ message: string; canProceed: boolean }> {
        // Verifica se o usuário existe
        const user = await this.userRepository.findOne({ where: { email } });
        
        if (!user) {
            return { 
                message: 'Se o e-mail existir, um código foi enviado.',
                canProceed: false 
            };
        }

        // Gera código aleatório de 4 dígitos
        const code = crypto.randomInt(1000, 9999).toString();

        // Define expiração de 10 minutos
        const expiresAt = new Date();
        expiresAt.setMinutes(expiresAt.getMinutes() + 10);

        // Invalida códigos anteriores deste email
        await this.passwordResetRepository.update(
            { email, used: false },
            { used: true }
        );

        // Salva o novo código no banco
        const passwordReset = this.passwordResetRepository.create({
            email,
            code,
            expiresAt,
        });
        await this.passwordResetRepository.save(passwordReset);

        await this.emailService.sendPasswordResetCode(email, code);

        return { 
            message: 'Código enviado com sucesso!',
            canProceed: true // Permite navegar para tela de validação
        };
    }

    async verifyResetCode(email: string, code: string): Promise<{ valid: boolean; message: string }> {
        const resetRequest = await this.passwordResetRepository.findOne({
            where: { email, code, used: false },
            order: { createdAt: 'DESC' },
        });

        if (!resetRequest) {
            return { valid: false, message: 'Código inválido' };
        }

        if (new Date() > resetRequest.expiresAt) {
            return { valid: false, message: 'Código expirado. Solicite um novo.' };
        }

        return { valid: true, message: 'Código validado com sucesso!' };
    }

    async resetPassword(email: string, code: string, newPassword: string): Promise<{ message: string }> {
        // Verifica novamente o código
        const verification = await this.verifyResetCode(email, code);
        if (!verification.valid) {
            throw new UnauthorizedException(verification.message);
        }

        // Busca o código
        const resetRequest = await this.passwordResetRepository.findOne({
            where: { email, code, used: false },
            order: { createdAt: 'DESC' },
        });

        if (!resetRequest) {
            throw new UnauthorizedException('Código não encontrado');
        }

        // Busca o usuário
        const user = await this.userRepository.findOne({ where: { email } });
        if (!user) {
            throw new UnauthorizedException('Usuário não encontrado');
        }

        // Atualiza a senha
        // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment, @typescript-eslint/no-unsafe-call, @typescript-eslint/no-unsafe-member-access
        const hashedPassword = await bcrypt.hash(newPassword, 10);
        // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
        user.password_hash = hashedPassword;
        await this.userRepository.save(user);

        // Marca o código como usado
        resetRequest.used = true;
        await this.passwordResetRepository.save(resetRequest);

        return { message: 'Senha alterada com sucesso!' };
    }

    private createToken(user: User): string {
        const payload: JwtPayload = { 
            sub: user.id,
            email: user.email,
            username: user.username
        };

        return this.jwtService.sign(payload)
    }

    private sanitizeUser(user: User): Partial<User> {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const { password_hash, ...sanitizedUser } = user;
        return sanitizedUser;
    }

    async getMe(id: string) {
        const user = await this.userRepository.findOneBy({ id });
        if (!user) {
            throw new UnauthorizedException('User not found');
        }
        return this.sanitizeUser(user);
    }
}