import { Module } from '@nestjs/common'
import { TypeOrmModule } from '@nestjs/typeorm'
import { LikesService } from './likes.service'
import { Like } from './likes.entity'
import { Post } from '../posts/posts.entity'
import { LikeController } from './likes.controller'

@Module({
  imports: [TypeOrmModule.forFeature([Like, Post])],
  controllers: [LikeController],
  providers: [LikesService],
  exports: [LikesService], 
})
export class LikesModule {}