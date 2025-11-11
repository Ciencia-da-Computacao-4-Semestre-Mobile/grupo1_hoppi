import { createParamDecorator, ExecutionContext } from '@nestjs/common';

export interface JwtPayloadUser {
  sub: string;
  email?: string;
  username?: string;
  [key: string]: unknown;
}

export const CurrentUser = createParamDecorator(
  (_data: unknown, ctx: ExecutionContext): JwtPayloadUser | undefined => {
    const request = ctx.switchToHttp().getRequest<{ user?: JwtPayloadUser }>();
    return request.user;
  },
);
