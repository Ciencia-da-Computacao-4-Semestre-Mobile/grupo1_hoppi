import { Injectable } from '@nestjs/common'
import { InjectRepository } from '@nestjs/typeorm'
import { Repository } from 'typeorm'
import { Post } from './posts.entity';
import type { CreatePostDTO } from './schemas/post.schema';

@Injectable()
export class PostsService {
  constructor(
    @InjectRepository(Post)
    private postsRepository: Repository<Post>,
  ) { }

  async create(data: CreatePostDTO) {
    const post = this.postsRepository.create({
      content: data.content,
      metadata: data.metadata ?? null,
      is_reply_to: data.is_reply_to
        ? ({ id: data.is_reply_to } as any)
        : null,
    });

    return this.postsRepository.save(post);
  }

  findAll() {
    return this.postsRepository.find({
      relations: ['author', 'likes', 'replies', 'postCount'],
    });
  }

  async findById(id: string) {
    return this.postsRepository.findOne({
      where: { id },
      relations: ['author', 'likes', 'replies', 'postCount'],
    });
  }

  async getReplies(id: string) {
    return this.postsRepository.find({
      where: { is_reply_to: { id } },
      relations: ['author'],
    });
  }

  async getPostsByUser(userId: string) {
    return this.postsRepository.find({
      where: { author: { id: userId } },
      relations: ['author', 'likes', 'replies', 'postCount'],
      order: { created_at: 'DESC' }
    });
  }

  async delete(id: string) {
    await this.postsRepository.delete(id);
  }
}
