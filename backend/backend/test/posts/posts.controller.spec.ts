import { Test, TestingModule } from '@nestjs/testing';
import { PostsController } from 'src/posts/posts.controller';
import { PostsService } from 'src/posts/posts.service';
import { Post as PostEntity } from 'src/posts/posts.entity';
import { CreatePostDTO } from 'src/posts/schemas/post.schema';
import { JwtAuthGuard } from 'src/auth/guards/jwt-auth.guard';
import { ExecutionContext } from '@nestjs/common';

const mockPostsService = {
  create: jest.fn(),
  findAll: jest.fn(),
  findById: jest.fn(),
  getReplies: jest.fn(),
  delete: jest.fn(),
};

const mockPostDto: CreatePostDTO = { content: 'Novo post testado' };

const mockPost: PostEntity = {
    id: 'test-id',
    content: 'Este é um post de teste',
    metadata: null,
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
const mockUser = { sub: 'user-id-from-jwt' };

const mockJwtAuthGuard = {
  canActivate: (context: ExecutionContext) => true,
};

describe('PostsController', () => {
  let controller: PostsController;
  let service: PostsService;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [PostsController],
      providers: [
        {
          provide: PostsService,
          useValue: mockPostsService,
        },
      ],
    })
      .overrideGuard(JwtAuthGuard)
      .useValue(mockJwtAuthGuard)
      .compile();

    controller = module.get<PostsController>(PostsController);
    service = module.get<PostsService>(PostsService);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should be defined', () => {
    expect(controller).toBeDefined();
  });

  describe('create', () => {
    it('should call postsService.create with DTO and user payload', async () => {
      mockPostsService.create.mockResolvedValue(mockPost);

      const req = { user: mockUser };
      const result = await controller.create(mockPostDto, req);

      expect(mockPostsService.create).toHaveBeenCalledWith(mockPostDto, mockUser);
      expect(result).toEqual(mockPost);
    });
  });

  describe('findAll', () => {
    it('should call postsService.findAll and return an array of posts', async () => {
      const mockArray = [mockPost];
      mockPostsService.findAll.mockResolvedValue(mockArray);

      const result = await controller.findAll();

      expect(mockPostsService.findAll).toHaveBeenCalled();
      expect(result).toEqual(mockArray);
    });
  });

  describe('findOne', () => {
    it('should call postsService.findById with the correct ID', async () => {
      const postId = 'post-id-1';
      mockPostsService.findById.mockResolvedValue(mockPost);

      const result = await controller.findOne(postId);

      expect(mockPostsService.findById).toHaveBeenCalledWith(postId);
      expect(result).toEqual(mockPost);
    });
  });

  describe('getReplies', () => {
    it('should call postsService.getReplies with the correct ID', async () => {
      const parentId = 'parent-id-1';
      const mockReplies = [mockPost];
      mockPostsService.getReplies.mockResolvedValue(mockReplies);

      const result = await controller.getReplies(parentId);
      
      expect(mockPostsService.getReplies).toHaveBeenCalledWith(parentId);
      expect(result).toEqual(mockReplies);
    });
  });

  describe('delete', () => {
    it('should call postsService.delete with the correct ID', async () => {
      const postId = 'delete-id-1';
      mockPostsService.delete.mockResolvedValue(undefined);

      await controller.delete(postId);

      expect(mockPostsService.delete).toHaveBeenCalledWith(postId);
    });
  });
});