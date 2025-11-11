import { Body, Controller, Delete, Get, Param, Patch, Post, Query, UseGuards } from '@nestjs/common'
import { CommunitiesService } from './communities.service';
import { Community } from './communities.entity';
import { AuthGuard } from 'src/auth/guards/auth.guard';
import { CreateCommunityDto } from './dto/create-community.dto';
import { CurrentUser } from 'src/auth/decorators/current-user.decorator';
// Usamos 'import type' para evitar emissão de metadata indevida em parâmetros decorados
import type { JwtPayloadUser } from 'src/auth/decorators/current-user.decorator';
import { ListMembersQueryDto } from './dto/list-members.query.dto';
import { ListRequestsQueryDto } from './dto/list-requests.query.dto';
import { CommunityRequestActionDto } from './dto/community-request-action.dto';
import { TransferOwnerDto } from './dto/transfer-owner.dto';

@Controller('communities')
export class CommunitiesController {
  constructor(private readonly communitiesService: CommunitiesService) {}

  @Get()
  findAll(): Promise<Community[]> {
    return this.communitiesService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string): Promise<Community> {
    return this.communitiesService.findOne(id);
  }

  @UseGuards(AuthGuard)
  @Post()
  create(@Body() dto: CreateCommunityDto, @CurrentUser() user: JwtPayloadUser) {
    return this.communitiesService.create(dto, { id: user.sub } as unknown as any);
  }

  @UseGuards(AuthGuard)
  @Patch(':id')
  update(@Param('id') id: string, @Body() dto: Partial<CreateCommunityDto>, @CurrentUser() user: JwtPayloadUser) {
    return this.communitiesService.update(id, dto, { id: user.sub } as unknown as any);
  }

  @UseGuards(AuthGuard)
  @Post(':id/join')
  join(@Param('id') id: string, @CurrentUser() user: JwtPayloadUser) {
    return this.communitiesService.join(id, { id: user.sub } as unknown as any);
  }

  @UseGuards(AuthGuard)
  @Delete(':id/leave')
  leave(@Param('id') id: string, @CurrentUser() user: JwtPayloadUser) {
    return this.communitiesService.leave(id, { id: user.sub } as unknown as any);
  }

  @UseGuards(AuthGuard)
  @Patch(':community/members/:user_id')
  updateMemberRole(
    @Param('community') communityId: string,
    @Param('user_id') userId: string,
    @Body('role') role: 'member' | 'moderator' | 'owner',
    @CurrentUser() user: JwtPayloadUser
  ) {
    return this.communitiesService.updateMemberRole(communityId, userId, role, { id: user.sub } as unknown as any);
  }

  // Members listing with pagination and role filter
  @UseGuards(AuthGuard)
  @Get(':id/members')
  listMembers(
    @Param('id') id: string,
    @Query() query: ListMembersQueryDto,
  ) {
    const page = Number(query.page ?? 1);
    const limit = Number(query.limit ?? 20);
    return this.communitiesService.listMembers(id, page, limit, query.role);
  }

  // Join requests listing (owner only)
  @UseGuards(AuthGuard)
  @Get(':id/requests')
  listRequests(
    @Param('id') id: string,
    @Query() query: ListRequestsQueryDto,
    @CurrentUser() user: JwtPayloadUser,
  ) {
    return this.communitiesService.listJoinRequests(id, query.status, { id: user.sub } as unknown as any);
  }

  // Approve/Reject a join request (owner only)
  @UseGuards(AuthGuard)
  @Patch(':id/requests/:request_id')
  actOnRequest(
    @Param('id') id: string,
    @Param('request_id') requestId: string,
    @Body() body: CommunityRequestActionDto,
    @CurrentUser() user: JwtPayloadUser,
  ) {
    return this.communitiesService.actOnJoinRequest(id, requestId, body.action, { id: user.sub } as unknown as any);
  }

  // Transfer ownership
  @UseGuards(AuthGuard)
  @Patch(':id/owner')
  transferOwner(
    @Param('id') id: string,
    @Body() body: TransferOwnerDto,
    @CurrentUser() user: JwtPayloadUser,
  ) {
    return this.communitiesService.transferOwnership(id, body.new_owner_user_id, { id: user.sub } as unknown as any);
  }
}
