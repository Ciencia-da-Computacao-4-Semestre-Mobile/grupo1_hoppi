import { Test, TestingModule } from '@nestjs/testing';
import { getRepositoryToken } from '@nestjs/typeorm';
import { Repository, ObjectLiteral } from 'typeorm';
import { PostsService } from 'src/posts/posts.service';
import { Post } from 'src/posts/posts.entity';
import { CreatePostDTO } from 'src/posts/schemas/post.schema';
import { FollowsService } from 'src/follows/follows.service';
import { FeedService } from 'src/posts/feed.service';

type MockRepository<T extends ObjectLiteral = any> = Partial<Record<keyof Repository<T>, jest.Mock>>;

const createMockRepository = (): MockRepository<Post> => ({
  create: jest.fn(),
  save: jest.fn(),
  find: jest.fn(),
  findOne: jest.fn(),
  delete: jest.fn(),
  increment: jest.fn(),
});

const mockUser = {
  sub: 'user-id-jwt-sub',
  username: 'testuser',
};

const mockFollowsService = {};
const mockFeedService = {};

describe('PostsService', () => {
  let service: PostsService;
  let repository: MockRepository<Post>;

  const mockPostDto: CreatePostDTO = {
    content: 'Este é um post de teste',
    metadata: { key: 'value' },
  };

  const mockReplyDto: CreatePostDTO = {
    content: 'Esta é uma resposta de teste',
    is_reply_to: 'parent-post-id',
  };

  const mockPost: Post = {
    id: 'test-id',
    content: 'Este é um post de teste',
    metadata: { key: 'value' },
    author: null as any,
    likes: [],
    replies: [],
    is_reply_to: null as any,
    postCount: null as any,
    created_at: new Date(),
    reply_count: 1000,
    is_deleted: false,
    community: null as any
  };

  const mockReplyPost: Post = {
    ...mockPost,
    id: 'reply-id',
    content: 'Esta é uma resposta de teste',
    is_reply_to: { id: 'parent-post-id' } as any,
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        PostsService,
        {
          provide: getRepositoryToken(Post),
          useValue: createMockRepository(),
        },
        {
          provide: FollowsService,
          useValue: mockFollowsService,
        },
        {
          provide: FeedService,
          useValue: mockFeedService,
        },
      ],
    }).compile();

    service = module.get<PostsService>(PostsService);
    repository = module.get<MockRepository<Post>>(getRepositoryToken(Post));
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });

  describe('create', () => {
    it('should create and save a new post successfully', async () => {
      repository.create?.mockReturnValue(mockPost);
      repository.save?.mockResolvedValue(mockPost);

      const result = await service.create(mockPostDto, mockUser);

      expect(repository.create).toHaveBeenCalledWith({
        content: mockPostDto.content,
        metadata: mockPostDto.metadata,
        author: { id: mockUser.sub } as any,
        is_reply_to: null,
      });

      expect(repository.save).toHaveBeenCalledWith(mockPost);
      expect(result).toEqual(mockPost);
      expect(repository.increment).not.toHaveBeenCalled();
    });

    it('should create a reply post successfully', async () => {
      repository.create?.mockReturnValue(mockReplyPost);
      repository.save?.mockResolvedValue(mockReplyPost);

      const result = await service.create(mockReplyDto, mockUser);

      expect(repository.create).toHaveBeenCalledWith({
        content: mockReplyDto.content,
        metadata: null,
        author: { id: mockUser.sub } as any,
        is_reply_to: { id: mockReplyDto.is_reply_to },
      });

      expect(repository.save).toHaveBeenCalledWith(mockReplyPost);
      expect(result).toEqual(mockReplyPost);
      expect(repository.increment).toHaveBeenCalledWith(
        { id: mockReplyDto.is_reply_to },
        'reply_count',
        1,
      );
    });
  });

  describe('findAll', () => {
    it('should return an array of posts with correct relations', async () => {
      const mockPostsArray = [mockPost, mockReplyPost];
      repository.find?.mockResolvedValue(mockPostsArray);

      const result = await service.findAll();

      expect(repository.find).toHaveBeenCalledWith({
        relations: ['author', 'likes', 'replies', 'postCount'],
      });

      expect(result).toEqual(mockPostsArray);
    });
  });

  describe('findById', () => {
    it('should return a single post by ID with correct relations', async () => {
      const postId = 'test-id';
      repository.findOne?.mockResolvedValue(mockPost);

      const result = await service.findById(postId);

      expect(repository.findOne).toHaveBeenCalledWith({
        where: { id: postId },
        relations: ['author', 'likes', 'replies', 'postCount'],
      });

      expect(result).toEqual(mockPost);
    });

    it('should return undefined if post is not found', async () => {
      repository.findOne?.mockResolvedValue(undefined);

      const result = await service.findById('non-existent-id');

      expect(repository.findOne).toHaveBeenCalled();
      expect(result).toBeUndefined();
    });
  });

  describe('getReplies', () => {
    it('should return replies for a given post ID with correct relations', async () => {
      const parentId = 'parent-post-id';
      const repliesArray = [mockReplyPost];
      repository.find?.mockResolvedValue(repliesArray);

      const result = await service.getReplies(parentId);

      expect(repository.find).toHaveBeenCalledWith({
        where: { is_reply_to: { id: parentId } },
        relations: ['author'],
      });

      expect(result).toEqual(repliesArray);
    });
  });

  describe('getPostsByUser', () => {
    it('should return posts for a given user ID with correct options', async () => {
      const userId = 'user-abc';
      const userPosts = [mockPost];
      repository.find?.mockResolvedValue(userPosts);

      const result = await service.getPostsByUser(userId);

      expect(repository.find).toHaveBeenCalledWith({
        where: { author: { id: userId } },
        relations: ['author', 'likes', 'replies', 'postCount'],
        order: { created_at: 'DESC' },
      });

      expect(result).toEqual(userPosts);
    });
  });

  describe('delete', () => {
    it('should call repository.delete with the correct ID', async () => {
      const postId = 'post-to-delete';
      repository.delete?.mockResolvedValue({ affected: 1, raw: [] });

      await service.delete(postId);

      expect(repository.delete).toHaveBeenCalledWith(postId);
    });
  });
});