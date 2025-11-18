import { z } from 'zod';

// Schema para Login
export const AuthLoginSchema = z.object({
  email: z.email({ message: 'Email inválido' }),
  password: z.string()
    .min(8, { message: 'A senha deve ter no mínimo 8 caracteres' })
    .regex(/[A-Z]/, { message: 'A senha deve conter pelo menos uma letra maiúscula' })
    .regex(/\d/, { message: 'A senha deve conter pelo menos um número' }),
});

export type AuthLoginDTO = z.infer<typeof AuthLoginSchema>;

// Schema para Registro
export const RegisterSchema = z.object({
  email: z.email({ message: 'Email inválido' }),
  password: z.string()
    .min(6, { message: 'A senha deve ter no mínimo 6 caracteres' })
    .regex(/[a-z]/, { message: 'A senha deve conter pelo menos uma letra minúscula' })
    .regex(/[A-Z]/, { message: 'A senha deve conter pelo menos uma letra maiúscula' })
    .regex(/\d/, { message: 'A senha deve conter pelo menos um número' }),
  username: z.string()
    .min(3, { message: 'O username deve ter no mínimo 3 caracteres' })
    .max(30, { message: 'O username deve ter no máximo 30 caracteres' }),
  display_name: z.string()
    .min(1, { message: 'O nome de exibição é obrigatório' })
    .max(100, { message: 'O nome de exibição deve ter no máximo 100 caracteres' }),
  birth_date: z.coerce.date({ 
    message: 'Data de nascimento inválida'
  }),
  institution: z.string()
    .min(1, { message: 'A instituição é obrigatória' })
    .max(100, { message: 'A instituição deve ter no máximo 100 caracteres' }),
});

export type RegisterDTO = z.infer<typeof RegisterSchema>;

// Schema para Forgot Password
export const ForgotPasswordSchema = z.object({
  email: z.email({ message: 'Email inválido' }),
});

export type ForgotPasswordDTO = z.infer<typeof ForgotPasswordSchema>;

// Schema para Verify Code
export const VerifyCodeSchema = z.object({
  email: z.email({ message: 'Email inválido' }),
  code: z.string()
    .length(4, { message: 'O código deve ter exatamente 4 dígitos' })
    .regex(/^\d{4}$/, { message: 'O código deve conter apenas números' }),
});

export type VerifyCodeDTO = z.infer<typeof VerifyCodeSchema>;

// Schema para Reset Password
export const ResetPasswordSchema = z.object({
  email: z.email({ message: 'Email inválido' }),
  code: z.string()
    .length(4, { message: 'O código deve ter exatamente 4 dígitos' })
    .regex(/^\d{4}$/, { message: 'O código deve conter apenas números' }),
  newPassword: z.string()
    .min(8, { message: 'A senha deve ter no mínimo 8 caracteres' })
    .regex(/[A-Z]/, { message: 'A senha deve conter pelo menos uma letra maiúscula' })
    .regex(/\d/, { message: 'A senha deve conter pelo menos um número' }),
});

export type ResetPasswordDTO = z.infer<typeof ResetPasswordSchema>;
