import { Injectable } from '@nestjs/common';
import { In, Repository } from 'typeorm';
import { Post } from './posts.entity';
import type { GetFeedQueryDTO } from './schemas/post.schema';
import { SanitizedPost, sanitizePostsArray } from 'src/common/utils/post-sanitizer.util';

@Injectable()
export class FeedService {
    constructor(
    ) { }

    async getMergedFeed(
        userId: string,
        query: GetFeedQueryDTO,
        postsRepository: Repository<Post>,
        followedUserIds: string[],
    ): Promise<SanitizedPost[]> {
        const { limit, cursor } = query;

        const followedLimit = Math.ceil(limit * 0.4);
        const trendingLimit = Math.floor(limit * 0.6);

        const followedPosts = await this.getPostsFromFollowed(
            postsRepository,
            followedUserIds,
            followedLimit,
            cursor,
        );

        const excludedPostIds = followedPosts.map(post => post.id);

        const trendingPosts = await this.getTrendingPosts(
            postsRepository,
            trendingLimit,
            excludedPostIds,
            cursor,
        );

        let feed = [...followedPosts, ...trendingPosts];

        feed = feed.filter((post, index, self) =>
            index === self.findIndex((t) => (t.id === post.id))
        );

        this.shuffleArray(feed);

        const finalFeedSlice = feed.slice(0, limit);
        return sanitizePostsArray(finalFeedSlice);
    }

    async getPostsFromFollowed(
        postsRepository: Repository<Post>,
        authorIds: string[],
        limit: number,
        cursor?: string
    ): Promise<Post[]> {
        const queryBuilder = postsRepository.createQueryBuilder('post')
            .leftJoinAndSelect('post.author', 'author')
            .leftJoinAndSelect('post.likes', 'likes')
            .where('post.author_id IN (:...authorIds)', { authorIds })
            .andWhere('post.is_deleted = :isDeleted', { isDeleted: false })
            .orderBy('post.created_at', 'DESC')
            .limit(limit);

        if (cursor) {
            const cursorPost = await postsRepository.findOne({ where: { id: cursor } });
            if (cursorPost) {
                queryBuilder.andWhere('post.created_at < :cursorDate', { cursorDate: cursorPost.created_at });
            }
        }

        return queryBuilder.getMany();
    }

    async getTrendingPosts(
        postsRepository: Repository<Post>,
        limit: number,
        excludedPostIds: string[],
        cursor?: string
    ): Promise<Post[]> {
        
        const sevenDaysAgo = new Date(); 
        sevenDaysAgo.setDate(sevenDaysAgo.getDate() - 7); 

        const queryBuilder = postsRepository.createQueryBuilder('post')
            .leftJoin('post.likes', 'like')
            .where('post.created_at >= :date', { date: sevenDaysAgo.toISOString() })
            .andWhere('post.is_deleted = :isDeleted', { isDeleted: false })
            .select(['post.id']) 
            .addSelect('COUNT("like"."post_id")', 'likeCount')
            .groupBy('post.id')
            .orderBy('"likeCount"', 'DESC') 
            .limit(limit);

        if (excludedPostIds.length > 0) {
            queryBuilder.andWhere('post.id NOT IN (:...excludedPostIds)', { excludedPostIds });
        }

        const rawResults: any[] = await queryBuilder.getRawMany(); 

        const postIds = rawResults.map((r) => r.post_id); 

        if (postIds.length === 0) return [];
        
        return postsRepository.find({
            where: { id: In(postIds) as any },
            relations: ['author', 'likes', 'postCount'],
        });
    }

    async getCommunityFeed(
        postsRepository: Repository<Post>,
        communityId: string,
        limit: number,
        cursor?: string
    ): Promise<Post[]> {
        const queryBuilder = postsRepository.createQueryBuilder('post')
            .leftJoinAndSelect('post.author', 'author')
            .where('post.community_id = :communityId', { communityId })
            .andWhere('post.is_deleted = :isDeleted', { isDeleted: false })
            .orderBy('post.created_at', 'DESC')
            .limit(limit);

        if (cursor) {
            const cursorPost = await postsRepository.findOne({ where: { id: cursor } });
            if (cursorPost) {
                queryBuilder.andWhere('post.created_at < :cursorDate', { cursorDate: cursorPost.created_at });
            }
        }

        return queryBuilder.getMany();
    }

    /* Implementação do algoritmo de Fisher-Yates para embaralhar o array */
    private shuffleArray<T>(array: T[]): void {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
    }
}