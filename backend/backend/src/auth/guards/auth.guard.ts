import { CanActivate, ExecutionContext, Injectable } from "@nestjs/common";
import { JwtService } from "@nestjs/jwt";
import type { AuthRequest } from "../interfaces/auth-request.interface";
import type { JwtPayloadUser } from "../decorators/current-user.decorator";

@Injectable()
export class AuthGuard implements CanActivate {
    constructor(
        private readonly jwtService: JwtService
    ) {}
    
    canActivate(context: ExecutionContext): boolean {
        const request = context.switchToHttp().getRequest<AuthRequest>();
        const authHeader = request.headers['authorization'];
        if (!authHeader || typeof authHeader !== 'string') {
            return false;
        }
        const token = this.extractToken(authHeader);

        if (!token) {
            return false;
        }

        try {
            const decoded = this.jwtService.verify(token) as JwtPayloadUser;
            request.user = decoded;
        } catch {
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
