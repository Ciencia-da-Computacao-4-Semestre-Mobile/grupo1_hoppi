import { Body, Controller, Post, HttpCode, HttpStatus, UseGuards, Req } from "@nestjs/common";
import { AuthService } from "./auth.service";
import { AuthLoginDto } from "./dto/auth.login.dto";
import { RegisterDto } from "./dto/auth.register.dto";
import { AuthGuard } from "./guards/auth.guard";
import { ForgotPasswordDto } from "./dto/forgot-password.dto";
import { VerifyCodeDto } from "./dto/verify-code.dto";
import { ResetPasswordDto } from "./dto/reset-password.dto";

@Controller("auth")
export class AuthController {

    constructor(
        private readonly authService: AuthService
    ) {}
    
    @Post("login")
    @HttpCode(HttpStatus.OK)
    async login(@Body() loginDto: AuthLoginDto) {
        return this.authService.login(loginDto);
    }

    @Post("register")
    @HttpCode(HttpStatus.CREATED)
    async register(@Body() registerDto: RegisterDto) {
        return this.authService.register(registerDto);
    }

    // NOVAS ROTAS PARA RECUPERAÇÃO DE SENHA
    
    @Post("forgot-password")
    @HttpCode(HttpStatus.OK)
    async forgotPassword(@Body() dto: ForgotPasswordDto) {
        return this.authService.forgotPassword(dto.email);
    }

    @Post("verify-reset-code")
    @HttpCode(HttpStatus.OK)
    async verifyCode(@Body() dto: VerifyCodeDto) {
        return this.authService.verifyResetCode(dto.email, dto.code);
    }

    @Post("reset-password")
    @HttpCode(HttpStatus.OK)
    async resetPassword(@Body() dto: ResetPasswordDto) {
        return this.authService.resetPassword(dto.email, dto.code, dto.newPassword);
    }

    @Post("me")
    @UseGuards(AuthGuard)
    async getMe(@Req() req: { user: { sub: string } }) {
        return this.authService.getMe(req.user.sub);
    }
}