import { IsDateString, IsEmail, IsString, Length, IsStrongPassword, IsDate } from "class-validator";

export class RegisterDto {
    @IsEmail()
    email: string;

    @IsStrongPassword({
        minLength: 6,
        minLowercase: 1,
        minUppercase: 1,
        minNumbers: 1,
        minSymbols: 0,
    })
    password: string;

    @IsString()
    @Length(3, 30)
    username: string;

    @IsString()
    @Length(1, 100)
    display_name: string; 

    @IsDate()
    birth_date: Date;

    @IsString()
    @Length(1, 100)
    institution: string;
}