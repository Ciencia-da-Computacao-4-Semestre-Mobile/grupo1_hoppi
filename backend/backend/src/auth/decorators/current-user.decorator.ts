import { createParamDecorator, ExecutionContext } from '@nestjs/common';
import type { AuthRequest } from '../interfaces/auth-request.interface';

export interface JwtPayloadUser {
  sub: string;
  email?: string;
  username?: string;
  [key: string]: unknown;
}

export const CurrentUser = createParamDecorator(
  (_data: unknown, ctx: ExecutionContext): JwtPayloadUser | undefined => {
    const request = ctx.switchToHttp().getRequest<AuthRequest>();
    return request.user;
  },
);
