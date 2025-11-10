import { BadRequestException, Injectable, NotFoundException, UnauthorizedException } from '@nestjs/common'
import { InjectRepository } from '@nestjs/typeorm'
import { Repository } from 'typeorm'
import { Community } from './communities.entity';
import { CommunityMember } from 'src/community_members/community_members.entity';
import { User } from 'src/users/users.entity';
import { CreateCommunityDto } from './dto/create-community.dto';

@Injectable()
export class CommunitiesService {
  constructor(
    @InjectRepository(Community)
    private communitiesRepository: Repository<Community>,
    @InjectRepository(CommunityMember)
    private communityMembersRepository: Repository<CommunityMember>,
  ) {}

  async create(dto: CreateCommunityDto, creator: User) {
    const exists = await this.communitiesRepository.findOne({ where: { name: dto.name } });
    if (exists) throw new BadRequestException('Community name already in use');

    const community = this.communitiesRepository.create({
      name: dto.name,
      description: dto.description ?? '',
      is_private: false,
      created_by: creator,
    });
    const saved = await this.communitiesRepository.save(community);

    // Add creator as owner member
    const member = this.communityMembersRepository.create({
      community: saved,
      user: creator,
      role: 'owner',
    });
    await this.communityMembersRepository.save(member);

    return saved;
  }

  findAll() {
    return this.communitiesRepository.find({ relations: ['created_by'] });
  }

  async findOne(id: string) {
    const community = await this.communitiesRepository.findOne({ where: { id }, relations: ['created_by'] });
    if (!community) throw new NotFoundException('Community not found');
    return community;
  }

  async update(id: string, dto: Partial<CreateCommunityDto>, actor: User) {
    const community = await this.findOne(id);
    // Only owner can update community basic info
    const owner = await this.communityMembersRepository.findOne({ where: { community: { id }, user: { id: actor.id }, role: 'owner' } });
    if (!owner) throw new UnauthorizedException('Only owner can update the community');

    Object.assign(community, {
      name: dto.name ?? community.name,
      description: dto.description ?? community.description,
    });
    return this.communitiesRepository.save(community);
  }

  async join(id: string, user: User) {
    const community = await this.findOne(id);
    const existing = await this.communityMembersRepository.findOne({ where: { community: { id }, user: { id: user.id } } });
    if (existing) return existing; // already a member
    const member = this.communityMembersRepository.create({ community, user, role: 'member' });
    return this.communityMembersRepository.save(member);
  }

  async leave(id: string, user: User) {
    const membership = await this.communityMembersRepository.findOne({ where: { community: { id }, user: { id: user.id } }, relations: ['community', 'user'] });
    if (!membership) throw new NotFoundException('Membership not found');
    if (membership.role === 'owner') throw new BadRequestException('Owner cannot leave. Transfer ownership first.');
    await this.communityMembersRepository.remove(membership);
    return { message: 'Left community' };
  }

  async updateMemberRole(communityId: string, targetUserId: string, role: 'member' | 'moderator' | 'owner', actor: User) {
    // Only owner can change roles
    const owner = await this.communityMembersRepository.findOne({ where: { community: { id: communityId }, user: { id: actor.id }, role: 'owner' } });
    if (!owner) throw new UnauthorizedException('Only owner can change roles');

    const membership = await this.communityMembersRepository.findOne({ where: { community: { id: communityId }, user: { id: targetUserId } }, relations: ['user', 'community'] });
    if (!membership) throw new NotFoundException('Member not found');
    membership.role = role;
    return this.communityMembersRepository.save(membership);
  }
}
