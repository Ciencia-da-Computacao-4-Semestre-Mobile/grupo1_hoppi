import { z } from 'zod'
import { Post } from 'src/posts/posts.entity'

export const ReturnPostCountSchema = z.object({
  post_id: z.uuid(),
  reply_count: z.number().int().min(0),
  like_count: z.number().int().min(0),
  
  post: z.any() as z.ZodType<Post>, 
})

export type ReturnPostCountDTO = z.infer<typeof ReturnPostCountSchema>