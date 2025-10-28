import { CommunityMember } from 'src/community_members/community_members.entity'
import { User } from 'src/users/users.entity'
import { z } from 'zod'

export const CreateCommunitySchema = z.object({
    name: z.string()
    .min(3, { message: "O nome da comunidade deve conter ao menos 3 caracteres." })
    .max(50, { message: "O nome da comunidade não deve ter mais que 50 caracteres." }),
    
    description: z.string()
    .min(5, { message: "A descrição deve ter ao menos 5 caracteres." })
    .max(200, { message: "A descrição não deve exceder 200 caracteres." }),

    is_private: z.boolean().optional()
})

export type CreateCommunityDTO = z.infer<typeof CreateCommunitySchema>


export const UpdateCommunitySchema = z.object({
    name: z.string()
    .min(3, { message: "O nome da comunidade deve conter ao menos 3 caracteres." })
    .max(50, { message: "O nome da comunidade não deve ter mais que 50 caracteres." })
    .optional(),
    
    description: z.string()
    .min(5, { message: "A descrição deve ter ao menos 5 caracteres." })
    .max(200, { message: "A descrição não deve exceder 200 caracteres." })
    .optional(),

    is_private: z.boolean().optional()
})

export type UpdateCommunityDTO = z.infer<typeof UpdateCommunitySchema>


export const ReturnCommunitySchema = z.object({
  id: z.uuid(),
  name: z.string(),
  description: z.string(),
  is_private: z.boolean(),
  created_at: z.date(),
  created_by: z.any() as z.ZodType<User | null>, 
  members: z.array(z.any() as z.ZodType<CommunityMember>).optional() 
})

export type ReturnCommunityDTO = z.infer<typeof ReturnCommunitySchema>

export const DeleteCommunityParamsSchema = z.object({
    id: z.uuid("ID de comunidade inválido."),
})

export type DeleteCommunityDTO = z.infer<typeof DeleteCommunityParamsSchema>