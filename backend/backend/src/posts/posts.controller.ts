import { Controller, Get, Post, Delete, Body, Param } from '@nestjs/common'
import { PostsService } from './posts.service'
import { Post as PostEntity } from './posts.entity';
import type { CreatePostDTO } from './schemas/post.schema';

@Controller('posts')
export class PostsController {
  constructor(private readonly postsService: PostsService) {}

  @Post()
  create(@Body() data: CreatePostDTO) {
    return this.postsService.create(data);
  }

  @Get()
  findAll(): Promise<PostEntity[]> {
    return this.postsService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string){
    return this.postsService.findById(id);
  }

  @Get(':id/replies')
  getReplies(@Param('id') id: string): Promise<PostEntity[]> {
    return this.postsService.getReplies(id);
  }

  @Delete(':id')
  delete(@Param('id') id: string): Promise<void> {
    return this.postsService.delete(id);
  }
}
