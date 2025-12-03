import { Controller, Get, UseGuards, Request, Param, Post, Body, BadRequestException, Patch } from '@nestjs/common'
import { UsersService } from './users.service'
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard'
import type { AuthRequest } from '../auth/interfaces/auth-request.interface'
import { ZodValidationPipe } from 'nestjs-zod'
import { CreateUserSchema, UpdatePasswordSchema, UpdateUserSchema } from './schemas/user.schema'
import type { CreateUserDTO, UpdatePasswordDTO, UpdateUserDTO } from './schemas/user.schema'

@Controller('users')
export class UsersController {
  constructor(private readonly usersService: UsersService){}

  @Post()
  create(
    @Body(new ZodValidationPipe(CreateUserSchema)) body: CreateUserDTO
  ) {
    return this.usersService.create(body)
  }

  @UseGuards(JwtAuthGuard)
  @Get('profile')
  getProfile(@Request() req: AuthRequest){

    if (!req.user) {
        throw new BadRequestException('User not authenticated')
    }

    return this.usersService.getByID(req.user.sub)
  }

  @Get('username')
  getByUsername(@Param('username') username: string){
    return this.usersService.getByUsername(username)
  }

  @UseGuards(JwtAuthGuard)
  @Patch('profile')
  updateProfile(
    @Request() req: AuthRequest,
    @Body(new ZodValidationPipe(UpdateUserSchema)) body: UpdateUserDTO
  ) {
    
    if (!req.user) {
        throw new BadRequestException('User not authenticated')
    }

    return this.usersService.update(req.user.sub, body)
  }

  @UseGuards(JwtAuthGuard)
  @Patch('profile/password')
  updatePassword(
    @Request() req: AuthRequest,
    @Body(new ZodValidationPipe(UpdatePasswordSchema)) body: UpdatePasswordDTO
  ){

    if (!req.user) {
        throw new BadRequestException('User not authenticated')
    }

    return this.usersService.updatePassword(req.user.sub, body)
  }

  @UseGuards(JwtAuthGuard)
  @Patch(':id/suspend')
  suspend(@Param('id') id: string){
    return this.usersService.suspend(id)
  }

  @Get(':id/posts')
  async getUserPosts(@Param('id') id: string) {
    return this.usersService.getPostsByUser(id)
  }

  @Get(':id/likes')
  async getUserLikes(@Param('id') id: string){
    return this.usersService.getUserLikes(id)
  }
  
  @UseGuards(JwtAuthGuard)
  @Get()
  getAllUsers() {
    return this.usersService.getAllUsers();
  }

  @Get(':id')
  async getUserById(@Param('id') id: string) {
    const user = await this.usersService.getByID(id);

    return {
        id: user.id,
        username: user.username,
        displayName: user.display_name,
        avatarKey: user.avatar_key,
        institution: user.institution,
        isSuspended: user.is_suspended
    };
  }
}
