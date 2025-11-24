import { Body, Controller, Post, HttpCode, HttpStatus, UseGuards, Req, BadRequestException } from "@nestjs/common";
import { AuthService } from "./auth.service";
import type { AuthRequest } from "./interfaces/auth-request.interface";
import { ZodValidationPipe } from 'nestjs-zod';
import { 
  AuthLoginSchema, 
  RegisterSchema, 
  ForgotPasswordSchema, 
  VerifyCodeSchema, 
  ResetPasswordSchema,
  type AuthLoginDTO,
  type RegisterDTO,
  type ForgotPasswordDTO,
  type VerifyCodeDTO,
  type ResetPasswordDTO
} from "./schemas/auth.schema";
import { JwtAuthGuard } from "./guards/jwt-auth.guard";

@Controller("auth")
export class AuthController {

    constructor(
        private readonly authService: AuthService
    ) {}
    
    @Post("login")
    @HttpCode(HttpStatus.OK)
    async login(@Body(new ZodValidationPipe(AuthLoginSchema)) loginDto: AuthLoginDTO) {
        return this.authService.login(loginDto);
    }

    @Post("register")
    @HttpCode(HttpStatus.CREATED)
    async register(@Body(new ZodValidationPipe(RegisterSchema)) registerDto: RegisterDTO) {
        return this.authService.register(registerDto);
    }

    // NOVAS ROTAS PARA RECUPERAÇÃO DE SENHA
    
    @Post("forgot-password")
    @HttpCode(HttpStatus.OK)
    async forgotPassword(@Body(new ZodValidationPipe(ForgotPasswordSchema)) dto: ForgotPasswordDTO) {
        return this.authService.forgotPassword(dto.email);
    }

    @Post("verify-reset-code")
    @HttpCode(HttpStatus.OK)
    async verifyCode(@Body(new ZodValidationPipe(VerifyCodeSchema)) dto: VerifyCodeDTO) {
        return this.authService.verifyResetCode(dto.email, dto.code);
    }

    @Post("reset-password")
    @HttpCode(HttpStatus.OK)
    async resetPassword(@Body(new ZodValidationPipe(ResetPasswordSchema)) dto: ResetPasswordDTO) {
        return this.authService.resetPassword(dto.email, dto.code, dto.newPassword);
    }

    @Post("me")
    @UseGuards(JwtAuthGuard)
    async getMe(@Req() req: AuthRequest) {
        if (!req.user) {
            throw new BadRequestException('User not authenticated');
        }
        return this.authService.getMe(req.user.sub);
    }
}