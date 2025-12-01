import { Injectable } from '@nestjs/common'
import { InjectRepository } from '@nestjs/typeorm'
import { Repository } from 'typeorm'
import { Post } from './posts.entity';
import type { CreatePostDTO, GetFeedQueryDTO } from './schemas/post.schema';
import { FollowsService } from 'src/follows/follows.service';
import { FeedService } from './feed.service';
import { SanitizedPost } from 'src/common/utils/post-sanitizer.util';
import { User } from 'src/users/users.entity';
import { JwtPayload } from 'src/auth/interfaces/auth-request.interface';

@Injectable()
export class PostsService {
  constructor(
    private followsService: FollowsService,
    private feedService: FeedService,
    @InjectRepository(Post)
    private postsRepository: Repository<Post>,
  ) { }

  async create(data: CreatePostDTO, user: JwtPayload) {
    const post = this.postsRepository.create({
      content: data.content,
      metadata: data.metadata ?? null,
      tag: data.tag,
      author: { id: user.sub } as User,
      is_reply_to: data.is_reply_to
        ? ({ id: data.is_reply_to } as Post)
        : undefined,
    });

    const savedPost = await this.postsRepository.save(post);

    const parentPostId = data.is_reply_to;

    if (parentPostId) {
      await this.postsRepository.increment(
        { id: parentPostId },
        'reply_count',
        1           
      );
    }

    return savedPost;
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

  /* Feed */
  async getFeed(userId: string, query: GetFeedQueryDTO): Promise<SanitizedPost[]> {
    const { strategy } = query;

    if (strategy === 'community') {
      if (!query.communityId) {
        throw new Error("ID da comunidade é obrigatório para a estratégia 'community'.");
      }
      return this.feedService.getCommunityFeed(
        this.postsRepository,
        query.communityId,
        query.limit,
        query.cursor
      );
    }


    const followedUserIds = await this.followsService.getFollowingIds(userId);
    followedUserIds.push(userId);

    return this.feedService.getMergedFeed(
      userId,
      query,
      this.postsRepository,
      followedUserIds,
    );
  }
}
