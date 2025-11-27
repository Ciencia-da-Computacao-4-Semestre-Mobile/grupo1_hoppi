import { z } from 'zod'
import { Post } from 'src/posts/posts.entity'
import { User } from 'src/users/users.entity'

export const CreateLikeSchema = z.object({
    post_id: z.uuid("ID de post inválido.")
})

export type CreateLikeDTO = z.infer<typeof CreateLikeSchema>


export const ReturnLikeSchema = z.object({
  user_id: z.uuid(),
  post_id: z.uuid(),
  liked_at: z.date(), 

  user: z.any() as z.ZodType<User>, 
  post: z.any() as z.ZodType<Post>,
})

export type ReturnLikeDTO = z.infer<typeof ReturnLikeSchema>


export const DeleteLikeSchema = z.object({
    post_id: z.uuid({ message: "ID de post inválido para operação 'deslike'."}),
})

export type DeleteLikeDTO = z.infer<typeof DeleteLikeSchema>