import { forwardRef, Module } from '@nestjs/common'
import { TypeOrmModule } from '@nestjs/typeorm'
import { PostsService } from './posts.service'
import { PostsController } from './posts.controller'
import { Post } from './posts.entity'
import { FollowsModule } from '../follows/follows.module'
import { FeedService } from './feed.service'

@Module({
  imports: [
    TypeOrmModule.forFeature([Post]),
    forwardRef(() => FollowsModule)
  ],
  controllers: [PostsController],
  providers: [PostsService, FeedService],
  exports: [PostsService],
})

export class PostsModule { }