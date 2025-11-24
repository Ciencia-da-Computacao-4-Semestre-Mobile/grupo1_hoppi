// import type { JwtPayloadUser } from '../decorators/current-user.decorator';
// import { string } from 'zod'

export interface JwtPayload {
  sub: string
  email: string
  username: string
}

export interface AuthRequest extends Request {
  user: {
    sub: string
    email: string
    username: string
  }
}

