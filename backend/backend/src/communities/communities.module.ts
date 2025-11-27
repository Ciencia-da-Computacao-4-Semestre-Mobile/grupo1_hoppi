import { Module } from '@nestjs/common'
import { TypeOrmModule } from '@nestjs/typeorm'
import { CommunitiesService } from './communities.service'
import { CommunitiesController } from './communities.controller'
import { Community } from './communities.entity'
import { CommunityMember } from 'src/community_members/community_members.entity'
import { CommunityJoinRequest } from './entities/community-join-request.entity'

@Module({
  imports: [TypeOrmModule.forFeature([Community, CommunityMember, CommunityJoinRequest])], 
  controllers: [CommunitiesController],              
  providers: [CommunitiesService]
})

export class CommunitiesModule {}