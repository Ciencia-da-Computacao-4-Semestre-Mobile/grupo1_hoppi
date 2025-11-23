import { Test, TestingModule } from '@nestjs/testing';
import { FeedService } from 'src/posts/feed.service';
import { Post } from 'src/posts/posts.entity';
import { GetFeedQueryDTO } from 'src/posts/schemas/post.schema';
import * as Sanitizer from 'src/common/utils/post-sanitizer.util';
import { Repository } from 'typeorm';

const mockUUID = '00000000-0000-0000-0000-000000000000';
const mockQueryBuilder = {
    leftJoinAndSelect: jest.fn().mockReturnThis(),
    leftJoin: jest.fn().mockReturnThis(),
    where: jest.fn().mockReturnThis(),
    andWhere: jest.fn().mockReturnThis(),
    orderBy: jest.fn().mockReturnThis(),
    limit: jest.fn().mockReturnThis(),
    select: jest.fn().mockReturnThis(),
    addSelect: jest.fn().mockReturnThis(),
    groupBy: jest.fn().mockReturnThis(),
    getMany: jest.fn(),
    getRawMany: jest.fn(),
};

interface MockedRepo extends Repository<Post> {
    findOne: jest.Mock;
    find: jest.Mock;
    createQueryBuilder: jest.Mock;
    In: jest.Mock;
}

const mockPostsRepository: MockedRepo = {
    In: jest.fn().mockImplementation((arr) => arr),
    findOne: jest.fn(),
    find: jest.fn(),
    createQueryBuilder: jest.fn().mockReturnValue(mockQueryBuilder),
    target: {} as any,
    manager: {} as any,
    metadata: {} as any,

} as unknown as MockedRepo;


const mockPosts: Post[] = [
    {
        id: 'p1',
        content: 'Post 1',
        author: { id: 'user1' } as any,
        is_deleted: false,
        created_at: new Date(2025, 0, 10),
        likes: [],
    } as unknown as Post,
    {
        id: 'p2',
        content: 'Post 2',
        author: { id: 'user2' } as any,
        is_deleted: false,
        created_at: new Date(2025, 0, 9),
        likes: []
    } as unknown as Post,
    {
        id: 'p3',
        content: 'Post 3',
        author: { id: 'user3' } as any,
        is_deleted: false,
        created_at: new Date(2025, 0, 8),
        likes: []
    } as unknown as Post,
    {
        id: 'p3',
        content: 'Post 4',
        author: { id: 'user' } as any,
        is_deleted: false,
        created_at: new Date(2025, 0, 7),
        likes: []
    } as unknown as Post,
];


const mockSanitizedPosts = mockPosts.map(p => ({ ...p, likesCount: p.likes.length }));

describe('FeedService', () => {
    let service: FeedService;

    const sanitizePostsArraySpy = jest.spyOn(Sanitizer, 'sanitizePostsArray');
    let shuffleArraySpy: jest.SpyInstance;


    beforeEach(async () => {
        const module: TestingModule = await Test.createTestingModule({
            providers: [FeedService],
        }).compile();

        service = module.get<FeedService>(FeedService);

        shuffleArraySpy = jest.spyOn(service as any, 'shuffleArray').mockImplementation((arr) => arr);
        sanitizePostsArraySpy.mockImplementation((arr) => arr as any);

        jest.clearAllMocks();
    });

    describe('getPostsFromFollowed', () => {
        const authorIds = [mockUUID, 'user-2'];
        const limit = 10;

        it('should build query correctly without cursor', async () => {
            mockQueryBuilder.getMany.mockResolvedValue([]);

            await service.getPostsFromFollowed(mockPostsRepository, authorIds, limit);

            expect(mockQueryBuilder.where).toHaveBeenCalledWith(
                'post.author_id IN (:...authorIds)',
                { authorIds },
            );
            expect(mockQueryBuilder.limit).toHaveBeenCalledWith(limit);
            expect(mockQueryBuilder.andWhere).toHaveBeenCalledWith(
                'post.is_deleted = :isDeleted',
                { isDeleted: false },
            );
        });

        it('should include cursor logic when cursor is provided', async () => {
            const cursorDate = new Date();
            mockPostsRepository.findOne.mockResolvedValue({ created_at: cursorDate });
            mockQueryBuilder.getMany.mockResolvedValue([]);

            await service.getPostsFromFollowed(mockPostsRepository, authorIds, limit, mockUUID);

            expect(mockPostsRepository.findOne).toHaveBeenCalledWith({ where: { id: mockUUID } });
            expect(mockQueryBuilder.andWhere).toHaveBeenCalledWith(
                'post.created_at < :cursorDate',
                { cursorDate: cursorDate },
            );
        });
    });

    describe('getTrendingPosts', () => {
        const limit = 5;
        const excludedIds = ['p_excluded_1', 'p_excluded_2'];
        const rawResults = [{ post_id: 'p3', likeCount: 10 }, { post_id: 'p4', likeCount: 5 }];

        it('should build trending query with correct date and ordering', async () => {
            mockQueryBuilder.getRawMany.mockResolvedValue(rawResults);
            mockPostsRepository.find.mockResolvedValue([]);

            await service.getTrendingPosts(mockPostsRepository, limit, excludedIds);

            expect(mockQueryBuilder.where).toHaveBeenCalledWith(
                'post.created_at >= :date',
                expect.any(Object)
            );
            expect(mockQueryBuilder.andWhere).toHaveBeenCalledWith(
                'post.id NOT IN (:...excludedPostIds)',
                { excludedPostIds: excludedIds }
            );
            expect(mockQueryBuilder.orderBy).toHaveBeenCalledWith('"likeCount"', 'DESC');
        });

        it('should execute second query using IDs from raw results', async () => {
            mockQueryBuilder.getRawMany.mockResolvedValue(rawResults);
            const finalPosts = [{ id: 'p3' }, { id: 'p4' }] as Post[];
            mockPostsRepository.find.mockResolvedValue(finalPosts);

            const result = await service.getTrendingPosts(mockPostsRepository, limit, excludedIds);
            expect(mockPostsRepository.find).toHaveBeenCalledWith({
                relations: ['author', 'likes', 'postCount'],
                where: {
                    id: expect.objectContaining({
                        _value: ['p3', 'p4'],
                        _type: 'in',
                    }),
                },
            });
            expect(result).toEqual(finalPosts);
        });

        it('should return empty array if no trending posts found', async () => {
            mockQueryBuilder.getRawMany.mockResolvedValue([]);
            const result = await service.getTrendingPosts(mockPostsRepository, limit, []);
            expect(mockPostsRepository.find).not.toHaveBeenCalled();
            expect(result).toEqual([]);
        });
    });

    describe('getMergedFeed', () => {
        const userId = 'u1';
        const followedUserIds = ['u1', 'u2', 'u3'];
        const query: GetFeedQueryDTO = { limit: 10, strategy: 'main' };

        let getFollowedSpy: jest.SpyInstance;
        let getTrendingSpy: jest.SpyInstance;

        beforeEach(() => {
            getFollowedSpy = jest.spyOn(service, 'getPostsFromFollowed');
            getTrendingSpy = jest.spyOn(service, 'getTrendingPosts');
        });

        it('should correctly divide limits (40/60) and merge results', async () => {
            getFollowedSpy.mockResolvedValue([mockPosts[0], mockPosts[1]]);
            getTrendingSpy.mockResolvedValue([mockPosts[2], mockPosts[3]]);

            await service.getMergedFeed(userId, query, mockPostsRepository, followedUserIds);

            expect(getFollowedSpy).toHaveBeenCalledWith(
                mockPostsRepository,
                followedUserIds,
                4,
                undefined,
            );
            expect(getTrendingSpy).toHaveBeenCalledWith(
                mockPostsRepository,
                6,
                ['p1', 'p2'],
                undefined,
            );

            expect(shuffleArraySpy).toHaveBeenCalled();
            expect(sanitizePostsArraySpy).toHaveBeenCalled();
        });

        it('should handle duplicate posts when merging (deduplication)', async () => {
            getFollowedSpy.mockResolvedValue([{ id: 'p1' }, { id: 'p2' }] as Post[]);
            getTrendingSpy.mockResolvedValue([{ id: 'p1' }, { id: 'p3' }] as Post[]);

            await service.getMergedFeed(userId, query, mockPostsRepository, followedUserIds);

            const feedArray = shuffleArraySpy.mock.calls[0][0];
            expect(feedArray.length).toBe(3);
            expect(feedArray.map(p => p.id)).toEqual(expect.arrayContaining(['p1', 'p2', 'p3']));
        });
    });

    describe('getCommunityFeed', () => {
        const communityId = mockUUID;
        const limit = 20;

        it('should build query correctly for community feed without cursor', async () => {
            mockQueryBuilder.getMany.mockResolvedValue([]);

            await service.getCommunityFeed(mockPostsRepository, communityId, limit);

            expect(mockQueryBuilder.where).toHaveBeenCalledWith(
                'post.community_id = :communityId',
                { communityId },
            );
            expect(mockQueryBuilder.limit).toHaveBeenCalledWith(limit);
            expect(mockQueryBuilder.orderBy).toHaveBeenCalledWith('post.created_at', 'DESC');
        });

        it('should apply cursor logic for community feed when cursor is provided', async () => {
            const cursorDate = new Date();
            mockPostsRepository.findOne.mockResolvedValue({ created_at: cursorDate });
            mockQueryBuilder.getMany.mockResolvedValue([]);

            await service.getCommunityFeed(mockPostsRepository, communityId, limit, mockUUID);

            expect(mockPostsRepository.findOne).toHaveBeenCalledWith({ where: { id: mockUUID } });
            expect(mockQueryBuilder.andWhere).toHaveBeenCalledWith(
                'post.created_at < :cursorDate',
                { cursorDate: cursorDate },
            );
        });
    });
});