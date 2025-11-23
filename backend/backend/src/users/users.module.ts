import { forwardRef, Module } from '@nestjs/common'
import { TypeOrmModule } from '@nestjs/typeorm'
import { UsersService } from './users.service'
import { UsersController } from './users.controller'
import { User } from './users.entity'
import { PostsModule } from '../posts/posts.module'
import { Post } from 'src/posts/posts.entity'

@Module({
  imports: [
    TypeOrmModule.forFeature([User, Post]),
    forwardRef(() => PostsModule)
  ], 
  controllers: [UsersController],              
  providers: [UsersService],
  exports: [UsersService]
})

export class UsersModule {}