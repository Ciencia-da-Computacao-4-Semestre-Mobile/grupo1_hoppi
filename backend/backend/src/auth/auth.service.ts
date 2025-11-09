import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { User } from "src/users/users.entity";
import { Repository } from "typeorm";
import * as bcrypt from 'bcrypt';
import { JwtService } from '@nestjs/jwt';
import { UnauthorizedException, ConflictException } from '@nestjs/common';
import { RegisterDto } from "./dto/auth.register.dto";
import { AuthLoginDto } from "./dto/auth.login.dto";
import { MailService } from "src/nodemailer/mailer.service";

@Injectable()
export class AuthService {
    constructor(
        @InjectRepository(User) private readonly userRepository: Repository<User>,
        private readonly jwtService: JwtService,
        private readonly mailService: MailService
    ) {}

    async login(dto: AuthLoginDto) {
        const user = await this.userRepository.findOneBy({ email: dto.email });

        if (!user || !(await bcrypt.compare(dto.password, user.password_hash))) {
            throw new UnauthorizedException('Invalid credentials');
        }

        const token = await this.createToken(user);

        return {
            access_token: token,
            user: this.sanitizeUser(user)
        };
    }

    async register(dto: RegisterDto) {
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

        const hashedPassword = await bcrypt.hash(dto.password, 10);

        const date = dto.birth_date.getDate() + 1;
        dto.birth_date.setDate(date);

        const newUser = this.userRepository.create({
            email: dto.email,
            password_hash: hashedPassword,
            username: dto.username,
            display_name: dto.display_name,
            birth_date: dto.birth_date,
            institution: dto.institution
        });

        const savedUser = await this.userRepository.save(newUser);
        const token = await this.createToken(savedUser);

        return {
            message: "Registration successful",
            access_token: token,
            user: this.sanitizeUser(savedUser)
        };
    }

    async forgot() {
        return "Password reset link sent";
    }

    private async createToken(user: User) {
        const payload = { 
            sub: user.id,
            email: user.email,
            username: user.username
        };

        return this.jwtService.sign(payload);
    }

    private sanitizeUser(user: User) {
        const { password_hash, ...sanitizedUser } = user;
        return sanitizedUser;
    }

    async getMe(id: string) {

        this.mailService.sendMail('email@teste.com');

      const user = await this.userRepository.findOneBy({ id: id });
      if (!user) {
          throw new UnauthorizedException('User not found');
      }

      return user;
    }
}