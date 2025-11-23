import {
  CreatePostSchema,
  GetFeedQuerySchema,
  FeedStrategySchema,
} from 'src/posts/schemas/post.schema';
import { z } from 'zod';
import { randomUUID } from 'crypto';


describe('CreatePostSchema', () => {
  const validData = {
    content: 'Este é um post válido com menos de 300 caracteres.',
    metadata: { source: 'web' },
    is_reply_to: randomUUID(),
  };

  it('should validate a post with all valid fields', () => {
    expect(() => CreatePostSchema.parse(validData)).not.toThrow();
  });

  it('should validate a post with only mandatory fields (content)', () => {
    const data = { content: 'Post simples' };
    expect(() => CreatePostSchema.parse(data)).not.toThrow();
  });

  it('should throw error when content is empty', () => {
    const data = { ...validData, content: '' };
    expect(() => CreatePostSchema.parse(data)).toThrow(
      'O conteúdo não pode estar vazio.',
    );
  });

  it('should throw error when content exceeds 300 characters', () => {
    const longContent = 'A'.repeat(301);
    const data = { ...validData, content: longContent };
    expect(() => CreatePostSchema.parse(data)).toThrow(
      'O conteúdo do post não deve exceder 300 caracteres.',
    );
  });

  it('should throw error if is_reply_to is not a valid UUID', () => {
    const data = { ...validData, is_reply_to: 'not-a-uuid' };
    expect(() => CreatePostSchema.parse(data)).toThrow(z.ZodError); 
  });

  it('should accept null or undefined for is_reply_to and metadata', () => {
    expect(() =>
      CreatePostSchema.parse({ content: 'Test' }),
    ).not.toThrow();
    expect(() =>
      CreatePostSchema.parse({
        content: 'Test',
        is_reply_to: null,
        metadata: null,
      }),
    ).not.toThrow();
  });
});

describe('GetFeedQuerySchema', () => {
  const mockCursor = randomUUID();
  const mockCommunityId = randomUUID();

  it('should return default values when no query parameters are provided', () => {
    const result = GetFeedQuerySchema.parse({});

    expect(result.limit).toBe(20);
    expect(result.strategy).toBe('main');
    expect(result.cursor).toBeUndefined();
    expect(result.communityId).toBeUndefined();
  });

  it('should successfully validate and transform custom limit and cursor', () => {
    const query = { limit: '50', cursor: mockCursor };
    const result = GetFeedQuerySchema.parse(query);

    expect(result.limit).toBe(50); 
    expect(result.cursor).toBe(mockCursor);
    expect(result.strategy).toBe('main');
  });

  it('should throw error if limit is less than 1', () => {
    const query = { limit: '0' };
    expect(() => GetFeedQuerySchema.parse(query)).toThrow(z.ZodError); 
  });

  it('should throw error if limit exceeds 100', () => {
    const query = { limit: '101' };
    expect(() => GetFeedQuerySchema.parse(query)).toThrow(
      'Limite não deve exceder 100.',
    );
  });
  
  it("should throw a refine error if strategy is 'community' but communityId is missing", () => {
    const query = { strategy: 'community' };
    expect(() => GetFeedQuerySchema.parse(query)).toThrow(
      "o ID da comunidade é obrigatório na estratégia 'community'",
    );
  });

  it("should validate successfully when strategy is 'community' and communityId is provided", () => {
    const query = { strategy: 'community', communityId: mockCommunityId };
    expect(() => GetFeedQuerySchema.parse(query)).not.toThrow();
    
    const result = GetFeedQuerySchema.parse(query);
    expect(result.strategy).toBe('community');
    expect(result.communityId).toBe(mockCommunityId);
  });
  
  it("should validate successfully when strategy is 'main' and communityId is present (but ignored)", () => {
    const query = { strategy: 'main', communityId: mockCommunityId };
    expect(() => GetFeedQuerySchema.parse(query)).not.toThrow();
  });
});

describe('FeedStrategySchema', () => {
    it('should validate "main"', () => {
        expect(() => FeedStrategySchema.parse('main')).not.toThrow();
    });

    it('should validate "community"', () => {
        expect(() => FeedStrategySchema.parse('community')).not.toThrow();
    });
    
    it('should throw error for invalid strategy', () => {
        expect(() => FeedStrategySchema.parse('invalid_strategy')).toThrow(
            'Uma estratégia de feed deve ser informada.',
        );
    });
});