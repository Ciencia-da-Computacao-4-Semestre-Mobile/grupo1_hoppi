import { IsEmail, IsString, MinLength, Matches, Length } from 'class-validator';

export class ResetPasswordDto {
  @IsEmail({}, { message: 'Email inválido' })
  email: string;

  @IsString()
  @Length(4, 4, { message: 'O código deve ter exatamente 4 dígitos' })
  @Matches(/^\d{4}$/, { message: 'O código deve conter apenas números' })
  code: string;

  @IsString()
  @MinLength(8, { message: 'A senha deve ter no mínimo 8 caracteres' })
  @Matches(/^(?=.*[A-Z])(?=.*\d).+$/, { 
    message: 'A senha deve conter pelo menos uma letra maiúscula e um número' 
  })
  newPassword: string;
}
