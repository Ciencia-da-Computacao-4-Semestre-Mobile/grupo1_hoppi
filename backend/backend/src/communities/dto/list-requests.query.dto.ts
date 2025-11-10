import { IsEnum, IsOptional } from 'class-validator';

export class ListRequestsQueryDto {
  @IsOptional()
  @IsEnum(['pending', 'approved', 'rejected'])
  status?: 'pending' | 'approved' | 'rejected';
}
