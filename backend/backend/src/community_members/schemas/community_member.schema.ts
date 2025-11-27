import { z } from 'zod'
import { Community } from 'src/communities/communities.entity'
import { User } from 'src/users/users.entity'

const MemberRoleSchema = z.enum(['member', 'owner', 'moderator'])

export const CreateCommunityMemberSchema = z.object({
  community_id: z.uuid({ message: "ID de comunidade inválido." }),
  role: MemberRoleSchema.default('member').optional()
})

export type CreateCommunityMemberDTO = z.infer<typeof CreateCommunityMemberSchema>


export const UpdateCommunityMemberSchema = z.object({
  role: MemberRoleSchema
})

export type UpdateCommunityMemberDTO = z.infer<typeof UpdateCommunityMemberSchema>


export const ReturnCommunityMemberSchema = z.object({
  id: z.uuid(),
  role: MemberRoleSchema,
  joined_at: z.date(), 
  community: z.any() as z.ZodType<Community>, 
  user: z.any() as z.ZodType<User>
})

export type ReturnCommunityMemberDTO = z.infer<typeof ReturnCommunityMemberSchema>