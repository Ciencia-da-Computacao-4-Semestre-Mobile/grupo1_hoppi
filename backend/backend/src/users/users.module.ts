import { Module } from '@nestjs/common'
import { TypeOrmModule } from '@nestjs/typeorm'
import { UsersService } from './users.service'
import { UsersController } from './users.controller'
import { User } from './users.entity'
import { PostsModule } from 'src/posts/posts.module'

@Module({
  imports: [
    TypeOrmModule.forFeature([User]),
    PostsModule
  ], 
  controllers: [UsersController],              
  providers: [UsersService]
})

export class UsersModule {}