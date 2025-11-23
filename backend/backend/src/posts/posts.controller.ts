import { Controller, Get, Post, Delete, Body, Param, UseGuards, Request, Query } from '@nestjs/common'
import { PostsService } from './posts.service'
import { Post as PostEntity } from './posts.entity';
import { type GetFeedQueryDTO, GetFeedQuerySchema, type CreatePostDTO } from './schemas/post.schema';
import { JwtAuthGuard } from 'src/auth/guards/jwt-auth.guard';
import type { AuthRequest } from 'src/auth/interfaces/auth-request.interface';
import { ZodValidationPipe } from 'nestjs-zod';
import { SanitizedPost } from 'src/common/utils/post-sanitizer.util';

@Controller('posts')
export class PostsController {
  constructor(private readonly postsService: PostsService) {}


  @UseGuards(JwtAuthGuard)
  @Get('feed')
  async getFeed(
    @Request() req: AuthRequest,
    @Query(new ZodValidationPipe(GetFeedQuerySchema)) query: GetFeedQueryDTO,
  ): Promise<SanitizedPost[]>{
    return this.postsService.getFeed(req.user.sub, query);
  }

  @UseGuards(JwtAuthGuard)
  @Post()
  create(
    @Body() data: CreatePostDTO,
    @Request() req: any
  ) {
    return this.postsService.create(data, req.user);
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
