import { Body, Controller, Post, HttpCode, HttpStatus, UseGuards, Req } from "@nestjs/common";
import { AuthService } from "./auth.service";
import { AuthLoginDto } from "./dto/auth.login.dto";
import { RegisterDto } from "./dto/auth.register.dto";
import { AuthGuard } from "./guards/auth.guard";

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

    @Post("forgot")
    @HttpCode(HttpStatus.OK)
    async forgot() {
        return this.authService.forgot()
    }

    @Post("me")
    @UseGuards(AuthGuard)
    async getMe(@Req() req) {
        return this.authService.getMe(req.user.sub);
    }
}