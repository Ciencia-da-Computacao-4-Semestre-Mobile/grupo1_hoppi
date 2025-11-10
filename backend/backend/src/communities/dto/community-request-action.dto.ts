import { IsEnum } from 'class-validator';

export class CommunityRequestActionDto {
  @IsEnum(['approve', 'reject'])
  action: 'approve' | 'reject';
}
