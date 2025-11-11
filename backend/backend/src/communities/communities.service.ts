import { BadRequestException, Injectable, NotFoundException, UnauthorizedException } from '@nestjs/common'
import { InjectRepository } from '@nestjs/typeorm'
import { Repository } from 'typeorm'
import { Community } from './communities.entity';
import { CommunityMember } from 'src/community_members/community_members.entity';
import { User } from 'src/users/users.entity';
import { CreateCommunityDto } from './dto/create-community.dto';
import { CommunityJoinRequest } from './entities/community-join-request.entity';

@Injectable()
export class CommunitiesService {
  constructor(
    @InjectRepository(Community)
    private communitiesRepository: Repository<Community>,
    @InjectRepository(CommunityMember)
    private communityMembersRepository: Repository<CommunityMember>,
    @InjectRepository(CommunityJoinRequest)
    private joinRequestsRepository: Repository<CommunityJoinRequest>,
  ) {}

  async create(dto: CreateCommunityDto, creator: User) {
    const exists = await this.communitiesRepository.findOne({ where: { name: dto.name } });
    if (exists) throw new BadRequestException('Community name already in use');

    const community = this.communitiesRepository.create({
      name: dto.name,
      description: dto.description ?? '',
      is_private: dto.is_private ?? false,
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

    // increment member_count
    await this.communitiesRepository.update(saved.id, { member_count: (saved.member_count ?? 0) + 1 });

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

    // Already member
    const existing = await this.communityMembersRepository.findOne({ where: { community: { id }, user: { id: user.id } } });
    if (existing) return existing;

    if (community.is_private) {
      // Check existing pending request
      const pending = await this.joinRequestsRepository.findOne({ where: { community: { id }, user: { id: user.id }, status: 'pending' } });
      if (pending) {
        return { message: 'Join request already pending', request_id: pending.id };
      }
      const request = this.joinRequestsRepository.create({ community, user, status: 'pending' });
      const saved = await this.joinRequestsRepository.save(request);
      return { message: 'Join request created', request_id: saved.id };
    }

    // Public community: add membership directly
    const member = this.communityMembersRepository.create({ community, user, role: 'member' });
    const saved = await this.communityMembersRepository.save(member);
    await this.communitiesRepository.update(community.id, { member_count: (community.member_count ?? 0) + 1 });
    return saved;
  }

  async leave(id: string, user: User) {
    const membership = await this.communityMembersRepository.findOne({ where: { community: { id }, user: { id: user.id } }, relations: ['community', 'user'] });
    if (!membership) throw new NotFoundException('Membership not found');
    if (membership.role === 'owner') throw new BadRequestException('Owner cannot leave. Transfer ownership first.');
    await this.communityMembersRepository.remove(membership);
    // decrement member_count
    const community = await this.findOne(id);
    const nextCount = Math.max(0, (community.member_count ?? 0) - 1);
    await this.communitiesRepository.update(id, { member_count: nextCount });
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

  async listMembers(communityId: string, page = 1, limit = 20, role?: 'member' | 'moderator' | 'owner') {
    const qb = this.communityMembersRepository
      .createQueryBuilder('m')
      .leftJoinAndSelect('m.user', 'user')
      .where('m.community_id = :communityId', { communityId });

    if (role) {
      qb.andWhere('m.role = :role', { role });
    }

    const total = await qb.getCount();
    const items = await qb
      .orderBy('m.joined_at', 'DESC')
      .skip((page - 1) * limit)
      .take(limit)
      .getMany();

    return { items, total, page, limit, pages: Math.ceil(total / limit) };
  }

  async listJoinRequests(communityId: string, status: 'pending' | 'approved' | 'rejected' | undefined, actor: User) {
    // Only owner can list requests
    const owner = await this.communityMembersRepository.findOne({ where: { community: { id: communityId }, user: { id: actor.id }, role: 'owner' } });
    if (!owner) throw new UnauthorizedException('Only owner can view join requests');

    const where: { community: { id: string }; status?: 'pending' | 'approved' | 'rejected' } = { community: { id: communityId } };
    if (status) where.status = status;
    return this.joinRequestsRepository.find({ where, relations: ['user'], order: { created_at: 'DESC' } });
  }

  async actOnJoinRequest(communityId: string, requestId: string, action: 'approve' | 'reject', actor: User) {
    // Only owner can act on requests
    const owner = await this.communityMembersRepository.findOne({ where: { community: { id: communityId }, user: { id: actor.id }, role: 'owner' } });
    if (!owner) throw new UnauthorizedException('Only owner can act on join requests');

    const request = await this.joinRequestsRepository.findOne({ where: { id: requestId, community: { id: communityId } }, relations: ['community', 'user'] });
    if (!request) throw new NotFoundException('Request not found');
    if (request.status !== 'pending') throw new BadRequestException('Request already processed');

    if (action === 'reject') {
      request.status = 'rejected';
      await this.joinRequestsRepository.save(request);
      return { message: 'Request rejected' };
    }

    // approve
    // ensure user not already a member
    const existing = await this.communityMembersRepository.findOne({ where: { community: { id: communityId }, user: { id: request.user.id } } });
    if (existing) {
      request.status = 'approved';
      await this.joinRequestsRepository.save(request);
      return existing;
    }

    return await this.communitiesRepository.manager.transaction(async (trx) => {
      request.status = 'approved';
      await trx.getRepository(CommunityJoinRequest).save(request);

      const membership = trx.getRepository(CommunityMember).create({ community: request.community, user: request.user, role: 'member' });
      const saved = await trx.getRepository(CommunityMember).save(membership);

      const community = await trx.getRepository(Community).findOne({ where: { id: communityId } });
      const nextCount = (community?.member_count ?? 0) + 1;
      await trx.getRepository(Community).update(communityId, { member_count: nextCount });

      return saved;
    });
  }

  async transferOwnership(communityId: string, newOwnerUserId: string, actor: User) {
    // validate actor is owner
    const ownerMembership = await this.communityMembersRepository.findOne({ where: { community: { id: communityId }, user: { id: actor.id }, role: 'owner' } });
    if (!ownerMembership) throw new UnauthorizedException('Only owner can transfer ownership');

    const targetMembership = await this.communityMembersRepository.findOne({ where: { community: { id: communityId }, user: { id: newOwnerUserId } } });
    if (!targetMembership) throw new NotFoundException('Target user is not a member');

    if (targetMembership.user?.id === actor.id) throw new BadRequestException('You already are the owner');

    return this.communitiesRepository.manager.transaction(async (trx) => {
      // downgrade current owner to moderator by default (or member)
      await trx.getRepository(CommunityMember).update(ownerMembership.id, { role: 'moderator' });
      await trx.getRepository(CommunityMember).update(targetMembership.id, { role: 'owner' });
      return { message: 'Ownership transferred' };
    });
  }
}
