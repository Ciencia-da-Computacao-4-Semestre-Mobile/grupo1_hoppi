import { Controller, Get, UseGuards, Request } from '@nestjs/common'
import { UsersService } from './users.service'
import { User } from './users.entity'
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard'

@Controller('users')
export class UsersController {
  constructor(private readonly usersService: UsersService) {}

  @Get()
  findAll(): Promise<User[]> {
    return this.usersService.findAll();
  }

  @UseGuards(JwtAuthGuard)
  @Get('me')
  getProfile(@Request() req: { user?: unknown }) {
    return req.user ?? null;
  }
}
