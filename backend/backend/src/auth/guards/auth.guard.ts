import { CanActivate, ExecutionContext, Injectable } from "@nestjs/common";
import { JwtService } from "@nestjs/jwt";

@Injectable()
export class AuthGuard implements CanActivate {
    constructor(
        private readonly jwtService: JwtService
    ) {}
    canActivate(context: ExecutionContext): boolean {

        const request = context.switchToHttp().getRequest();
        const authHeader = request.headers['authorization'];
        if (!authHeader) {
            return false;
        }
        const token = this.extractToken(authHeader);

        if (!token) {
            return false;
        }

        try {
            this.jwtService.verify(token);
            request.user = this.jwtService.decode(token);
        } catch (error) {
            return false;
        }

        return true;
    }

    extractToken(authHeader: string): string | null {
        const parts = authHeader.split(' ');
        if (parts.length === 2 && parts[0] === 'Bearer') {
            return parts[1];
        }
        return null;
    }
}