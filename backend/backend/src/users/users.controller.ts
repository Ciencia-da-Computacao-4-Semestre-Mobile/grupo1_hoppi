import { Controller, Get, UseGuards, Request, Param } from '@nestjs/common'
import { UsersService } from './users.service'
import { User } from './users.entity'
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard'
import type { AuthRequest } from '../auth/interfaces/auth-request.interface'

@Controller('users')
export class UsersController {
  constructor(private readonly usersService: UsersService) { }

  @Get()
  findAll(): Promise<User[]> {
    return this.usersService.findAll();
  }

  @UseGuards(JwtAuthGuard)
  @Get('me')
  getProfile(@Request() req: AuthRequest) {
    return req.user ?? null;
  }

  @Get('posts/:id')
  async getUserPosts(@Param('id') id: string) {
    return this.usersService.getUserPosts(id);
  }
}
