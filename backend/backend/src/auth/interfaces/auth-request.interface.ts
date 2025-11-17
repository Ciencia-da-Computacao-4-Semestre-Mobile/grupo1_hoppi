import type { JwtPayloadUser } from '../decorators/current-user.decorator';

export interface AuthRequest {
  user?: JwtPayloadUser;
  headers: Record<string, string | string[] | undefined>;
  [key: string]: unknown;
}

