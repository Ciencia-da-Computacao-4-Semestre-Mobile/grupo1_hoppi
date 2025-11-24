import { Injectable, UnauthorizedException } from '@nestjs/common'
import { PassportStrategy } from '@nestjs/passport'
import { ExtractJwt, Strategy } from 'passport-jwt'
import { env } from 'src/config/env'
import { User } from 'src/users/users.entity'
import type { JwtPayload } from './interfaces/auth-request.interface'
import { UsersService } from 'src/users/users.service'

@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy, 'jwt'){
    constructor(private readonly userService: UsersService){
        super({
            jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
            ignoreExpiration: false,
            secretOrKey: env.JWT_SECRET
        })
    }

    async validate(payload: JwtPayload): Promise<Partial<User>>{

        const user = await this.userService.getByID(payload.sub)
        if (!user) {
        throw new UnauthorizedException()
        }

        return {
            id: payload.sub,
            email: payload.email,
            username: payload.username
        } as Partial<User>
    }
}