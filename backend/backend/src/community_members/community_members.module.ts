import { Module } from '@nestjs/common'
import { TypeOrmModule } from '@nestjs/typeorm'
import { CommunityMembersService } from './community_members.service'
import { CommunityMember } from './community_members.entity'

@Module({
  imports: [TypeOrmModule.forFeature([CommunityMember])],
  providers: [CommunityMembersService],
  exports: [CommunityMembersService], 
})
export class CommunityMembersModule {}