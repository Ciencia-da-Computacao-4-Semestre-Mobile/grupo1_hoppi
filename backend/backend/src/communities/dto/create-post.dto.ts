import { IsNotEmpty, IsString, IsOptional, IsArray } from 'class-validator';

export class CreatePostDto {
    @IsNotEmpty()
    @IsString()
    content: string;

    @IsArray()
    @IsOptional()
    tags?: string[];
}