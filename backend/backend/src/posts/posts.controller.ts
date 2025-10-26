import { Controller, Get } from '@nestjs/common'
import { PostsService } from './posts.service'
import { Post } from './posts.entity';

@Controller('posts')
export class PostsController {
  constructor(private readonly postsService: PostsService) {}

  @Get()
  findAll(): Promise<Post[]> {
    return this.postsService.findAll();
  }
}
