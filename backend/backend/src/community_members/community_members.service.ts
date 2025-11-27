import { Injectable } from '@nestjs/common'
import { InjectRepository } from '@nestjs/typeorm'
import { Repository } from 'typeorm'
import { CommunityMember } from './community_members.entity'

@Injectable()
export class CommunityMembersService {
  constructor(
    @InjectRepository(CommunityMember)
    private communityMembersRepository: Repository<CommunityMember>,
  ) {}

}