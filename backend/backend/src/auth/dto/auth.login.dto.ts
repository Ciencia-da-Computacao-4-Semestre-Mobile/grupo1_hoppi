import { IsEmail, IsStrongPassword } from "class-validator";

export class AuthLoginDto {
    @IsEmail()
    email: string;

    @IsStrongPassword({
        minLength: 8,
        minLowercase: 0,
        minUppercase: 1,
        minNumbers: 1,
        minSymbols: 0,
    })
    password: string;
}