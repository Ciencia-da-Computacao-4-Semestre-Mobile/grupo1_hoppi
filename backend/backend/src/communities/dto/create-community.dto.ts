import { IsNotEmpty, IsString, IsOptional, IsArray } from 'class-validator';

export class CreateCommunityDto {
    @IsNotEmpty()
    @IsString()
    name: string;

    @IsString()
    @IsOptional()
    description?: string;

    @IsString()
    @IsOptional()
    category?: string;

    @IsArray()
    @IsOptional()
    tags?: string[];
}