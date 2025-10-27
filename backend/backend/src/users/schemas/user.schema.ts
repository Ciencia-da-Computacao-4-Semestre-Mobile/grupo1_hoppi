import { z } from 'zod'

// regex de validação para a o birth_date
const DateStringSchema = z.string().regex(/^\d{4}-\d{2}-\d{2}$/, 'Formato de data inválido. Formato esperado: YYYY-MM-DD.')

export const UserBaseSchema = z.object({
  username: z.string()
    .min(3, { message: "O nome de usuário deve ter pelo menos 3 caracteres." })
    .max(30, { message: "O nome de usuário não deve ter mais que 30 caracteres." }),
    
  display_name: z.string()
    .min(1, { message: "O nome de exibição não pode estar vazio." })
    .max(30, { message: "O nome de exibição não deve ter mais que 30 caracteres." }),
    
  avatar_key: z.string()
    .default('avatar_1'),

  email: z.email({ message: "Formato de e-mail inválido." }),
  birth_date: DateStringSchema,

  institution: z.string()
    .min(3, { message: "O nome da instituição deve ter ao menos 3 caracteres." })
    .max(100, { message: "O nome da instituição não deve ter mais que 100 caracteres." }),
})

export const CreateUserSchema = UserBaseSchema.extend({
  password: z.string()
    .min(8, { message: "A senha deve ter pelo menos 8 caracteres." })
    .max(100, { message: "A senha não deve ter mais que 100 caracteres." }),
})

export type CreateUserDTO = z.infer<typeof CreateUserSchema>


export const UpdateUserSchema = UserBaseSchema
  .partial()
  .omit({ 
    // campos que terão endpoints de atualização separados ou não serão atualizados
    email: true,
    birth_date: true,
})

export type UpdateUserDTO = z.infer<typeof UpdateUserSchema>


export const LoginSchema = z.object({
  email: z.email().min(1, "E-mail é um campo obrigatório."),
  password: z.string().min(1, "Senha é um campo obrigatório."),
})

export type LoginDTO = z.infer<typeof LoginSchema>


export const UpdatePasswordSchema = z.object({
  current_password: z.string().min(8).max(100),
  new_password: z.string()
  .min(8, "A senha deve ter pelo menos 8 caracteres.")
  .max(100, "A senha não deve ter mais que 100 caracteres.")
})

.refine((data) => data.new_password !== data.current_password, {
    message: "A nova senha deve ser diferente da senha atual.",
    path: ["new_password"],
})

export type UpdatePasswordDTO = z.infer<typeof UpdatePasswordSchema>