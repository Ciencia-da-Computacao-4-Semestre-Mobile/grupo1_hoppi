import { z } from 'zod'
import { User } from 'src/users/users.entity'

export const CreateFollowSchema = z.object({
    //follower é injetado pelo servidor
    followee_id: z.uuid("Formato de ID inválido.")
})

export type CreateFollowDTO = z.infer<typeof CreateFollowSchema>


export const ReturnFollowSchema = z.object({
    follower_id: z.uuid(),
    followee_id: z.uuid(),
    follower: z.any() as z.ZodType<User>,
    followee: z.any() as z.ZodType<User>,
    followed_at: z.date()
})

export type ReturnFollowDTO = z.infer<typeof ReturnFollowSchema>


export const DeleteFollowSchema = z.object({
    followee_id: z.uuid({ message: "Formato de ID inválido." }),
})

export type DeleteFollowDTO = z.infer<typeof DeleteFollowSchema>