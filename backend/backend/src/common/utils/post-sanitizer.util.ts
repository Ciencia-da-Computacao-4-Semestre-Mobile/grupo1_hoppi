import type { Post } from 'src/posts/posts.entity';
import { mapToPublicUserDTO, PublicUserDTO } from 'src/users/schemas/user.schema';

export interface SanitizedPost extends Omit<Post, 'author' | 'replies'> {
    author: PublicUserDTO;
    replies: SanitizedPost[];
}

function sanitizePost(post: Post): SanitizedPost {
    const sanitizedPost: SanitizedPost = {
        ...post,
        author: post.author ? mapToPublicUserDTO(post.author) : null,
        replies: post.replies 
            ? post.replies.map(reply => sanitizePost(reply))
            : [] 
    } as SanitizedPost;

    return sanitizedPost;
}

export function sanitizePostsArray(posts: Post[]): SanitizedPost[] {
    if (!posts) return [];
    
    return posts.map(post => sanitizePost(post));
}