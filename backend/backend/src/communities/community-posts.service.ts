import { Injectable, NotFoundException, UnauthorizedException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { CommunityPost } from './entities/community-post.entity';
import { User } from '../users/users.entity';
import { CommunityMember } from './entities/community-member.entity';
import { CreatePostDTO } from 'src/posts/schemas/post.schema';

// REVISAR

@Injectable()
export class CommunityPostsService {
    constructor(
        @InjectRepository(CommunityPost)
        private readonly postsRepository: Repository<CommunityPost>,
        @InjectRepository(CommunityMember)
        private readonly membersRepository: Repository<CommunityMember>,
    ) {}

    async createPost(
    communityId: number,
        createPostDto: CreatePostDTO,
        author: User
    ): Promise<CommunityPost> {
        // Verify membership and permissions
        const membership = await this.membersRepository.findOne({
            where: {
                community: { id: communityId.toString() },
                user: { id: author.id.toString() }
            }
        });

        if (!membership) {
            throw new UnauthorizedException('Must be a member to post');
        }

        const post = this.postsRepository.create({
            ...createPostDto,
            community: { id: communityId.toString() },
            author
        });

        return this.postsRepository.save(post);
    }

    async findCommunityPosts(
        communityId: number,
    page = 1,
    limit = 10
    ): Promise<{ posts: CommunityPost[]; total: number }> {
        const [posts, total] = await this.postsRepository.findAndCount({
            where: { community: { id: communityId.toString() } },
            relations: ['author'],
            order: { createdAt: 'DESC' },
            take: limit,
            skip: (page - 1) * limit
        });

        return { posts, total };
    }

    async likePost(postId: number): Promise<CommunityPost> {
        const post = await this.postsRepository.findOne({
            where: { id: postId }
        });

        if (!post) {
            throw new NotFoundException('Post not found');
        }

        post.likes += 1;
        return this.postsRepository.save(post);
    }
}