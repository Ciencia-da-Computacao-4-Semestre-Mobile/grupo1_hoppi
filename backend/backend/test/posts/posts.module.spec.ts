import { Test, TestingModule } from '@nestjs/testing';
import { getRepositoryToken } from '@nestjs/typeorm';
import { PostsModule } from 'src/posts/posts.module';
import { PostsService } from 'src/posts/posts.service';
import { PostsController } from 'src/posts/posts.controller';
import { Post } from 'src/posts/posts.entity';
import { FollowsService } from 'src/follows/follows.service';
import { FeedService } from 'src/posts/feed.service';

const mockPostRepository = {}
const mockFollowsService = {};
const mockFeedService = {};

describe('PostsModule', () => {
  let module: TestingModule;

  beforeEach(async () => {
    module = await Test.createTestingModule({
      controllers: [PostsController],
      providers: [
        PostsService,
        {
          provide: getRepositoryToken(Post),
          useValue: mockPostRepository,
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
  });


  it('should be defined', () => {
    expect(module).toBeDefined();
  });

  it('should compile the module structure successfully', () => {
    expect(module).toBeInstanceOf(TestingModule);
  });

  it('should have PostsService available as a provider', () => {
    const service = module.get<PostsService>(PostsService);
    expect(service).toBeDefined();
    expect(service).toBeInstanceOf(PostsService);
  });

  it('should have PostsController available', () => {
    const controller = module.get<PostsController>(PostsController);
    expect(controller).toBeDefined();
    expect(controller).toBeInstanceOf(PostsController);
  });
});