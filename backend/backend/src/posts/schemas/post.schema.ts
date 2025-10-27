import { z } from 'zod'
import { User } from 'src/users/users.entity'
import { Post } from '../posts.entity'
import { Like } from 'src/likes/likes.entity'

const MetadataSchema = z.record(z.string(), z.any()).optional().nullable()

export const CreatePostSchema = z.object({
    content: z.string()
    .min(1, "O conteúdo não pode estar vazio.")
    .max(300, "O conteúdo do post não deve exceder 300 caracteres."),

    is_reply_to: z.uuid().optional().nullable(),
    metadata: MetadataSchema
})

export type CreatePostDTO = z.infer<typeof CreatePostSchema>


export const ReturnPostSchema = z.object({
    id: z.uuid(),
    content: z.string(),
    created_at: z.date(),
    reply_count: z.number().int().default(0),
    is_deleted: z.boolean().default(false),
    metadata: MetadataSchema,

    author: z.any() as z.ZodType<User>,
    is_reply_to: z.any() as z.ZodType<Post | null | undefined>,
    replies: z.array(z.any() as z.ZodType<Post>).optional(),
    likes: z.array(z.any() as z.ZodType<Like>).optional(),
    post_count: z.any().optional()
})

export type ReturnPostDTO = z.infer<typeof ReturnPostSchema>