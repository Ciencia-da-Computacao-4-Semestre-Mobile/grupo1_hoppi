import { IsInt, IsOptional, Min, IsEnum } from 'class-validator';

export class ListMembersQueryDto {
  @IsOptional()
  @IsInt()
  @Min(1)
  page?: number = 1;

  @IsOptional()
  @IsInt()
  @Min(1)
  limit?: number = 20;

  @IsOptional()
  @IsEnum(['member', 'moderator', 'owner'])
  role?: 'member' | 'moderator' | 'owner';
}
