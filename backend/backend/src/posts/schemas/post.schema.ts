import { z } from 'zod'
import { User } from 'src/users/users.entity'
import { Post } from '../posts.entity'
import { Like } from 'src/likes/likes.entity'

const MetadataSchema = z.record(z.string(), z.any()).optional().nullable()

export const CreatePostSchema = z.object({
    content: z.string()
    .min(1, "O conteúdo não pode estar vazio.")
    .max(300, "O conteúdo do post não deve exceder 300 caracteres."),

    is_reply_to: z.uuid().optional(),
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

// schemas do feed

export const FeedStrategySchema = z.enum(['main', 'community'], {
    message: "Uma estratégia de feed deve ser informada.",
})

export const GetFeedQuerySchema = z.object({

    // aqui é definido um limite de posts a serem carregados
    limit: z.string()
    .default('20')
    .transform((val) => Number(val))
    .pipe(z.number().int().min(1).max(100, "Limite não deve exceder 100.")),
    
    // aqui, coleta o id do último post visto pelo usuário, para que possa carregar mais
    cursor: z.uuid("ID de cursor inválido.").optional(),
  
    strategy: FeedStrategySchema.default('main'),
    communityId: z.uuid("ID de comunidade inválido.").optional(),

}).refine((data) => {
    if (data.strategy === 'community' && !data.communityId) {
        return false;
    }
    return true;
}, {
    message: "o ID da comunidade é obrigatório na estratégia 'community'",
    path: ["communityId"],
})

export type GetFeedQueryDTO = z.infer<typeof GetFeedQuerySchema>