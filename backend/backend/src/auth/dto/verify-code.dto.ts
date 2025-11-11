import { IsEmail, IsString, Length, Matches } from 'class-validator';

export class VerifyCodeDto {
  @IsEmail({}, { message: 'Email inválido' })
  email: string;

  @IsString()
  @Length(4, 4, { message: 'O código deve ter exatamente 4 dígitos' })
  @Matches(/^\d{4}$/, { message: 'O código deve conter apenas números' })
  code: string;
}
