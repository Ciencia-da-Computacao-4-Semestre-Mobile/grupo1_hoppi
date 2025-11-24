import { z } from 'zod'
import { User } from 'src/users/users.entity';
import { BadRequestException, Body, Controller, Delete, Get, Param, Patch, Post, Query, UseGuards } from '@nestjs/common'
import { CommunitiesService } from './communities.service';
import { Community } from './communities.entity';
import { JwtAuthGuard } from 'src/auth/guards/jwt-auth.guard';
import { CurrentUser } from 'src/auth/decorators/current-user.decorator';
import type { JwtPayloadUser } from 'src/auth/decorators/current-user.decorator';
import { ZodValidationPipe } from 'nestjs-zod';
import { CommunityRequestActionSchema, CreateCommunitySchema, DeleteCommunityParamsSchema, ListMembersQuerySchema, ListRequestsQuerySchema, TransferOwnerSchema, UpdateCommunitySchema } from './schemas/community.schema'
import type { CreateCommunityDTO, UpdateCommunityDTO } from './schemas/community.schema'

@Controller('communities')
export class CommunitiesController {
  constructor(private readonly communitiesService: CommunitiesService) {}

  @Get()
  findAll(): Promise<Community[]> {
    return this.communitiesService.findAll();
  }

  @Get(':id')
  async findOne(@Param(new ZodValidationPipe(DeleteCommunityParamsSchema)) params: { id: string }){
    return await this.communitiesService.findOne(params.id)
  }

  @UseGuards(JwtAuthGuard)
  @Post()
  async create(
    @Body(new ZodValidationPipe(CreateCommunitySchema)) 
    data: CreateCommunityDTO,
    @CurrentUser() user: JwtPayloadUser
  ) {
    return await this.communitiesService.create(data, { id: user.sub } as User)
  }

  @UseGuards(JwtAuthGuard)
  @Patch(':id')
  async update(@Param(new ZodValidationPipe(DeleteCommunityParamsSchema)) params: { id: string }, 
  @Body(new ZodValidationPipe(UpdateCommunitySchema)) data: UpdateCommunityDTO, @CurrentUser() user: JwtPayloadUser) {
    return await this.communitiesService.update(params.id, data, { id: user.sub } as User)
  }

  @UseGuards(JwtAuthGuard)
  @Post(':id/join')
  join(@Param('id') id: string, @CurrentUser() user: JwtPayloadUser) {
    return this.communitiesService.join(id, { id: user.sub } as User);
  }

  @UseGuards(JwtAuthGuard)
  @Delete(':id/leave')
  leave(@Param('id') id: string, @CurrentUser() user: JwtPayloadUser) {
    return this.communitiesService.leave(id, { id: user.sub } as User);
  }

  @UseGuards(JwtAuthGuard)
  @Patch(':community/members/:user_id')
  updateMemberRole(
    @Param('community') communityId: string,
    @Param('user_id') userId: string,
    @Body('role') role: 'member' | 'moderator' | 'owner',
    @CurrentUser() user: JwtPayloadUser
  ) {
    return this.communitiesService.updateMemberRole(communityId, userId, role, { id: user.sub } as unknown as any);
  }

  @UseGuards(JwtAuthGuard)
  @Get(':id/members')
  listMembers(
    @Param('id') id: string,
    @Query() query: any,
  ) {
    const parsed = ListMembersQuerySchema.safeParse(query)
    if (!parsed.success) {
      throw new BadRequestException(z.treeifyError(parsed.error))
    }

    const data = parsed.data
    const page = Number(data.page ?? 1)
    const limit = Number(data.limit ?? 20)

    return this.communitiesService.listMembers(id, page, limit, data.role)
  }

  @UseGuards(JwtAuthGuard)
  @Get(':id/requests')
  listRequests(
    @Param('id') id: string,
    @Query() query: any,
    @CurrentUser() user: JwtPayloadUser,
  ) {
    const parsed = ListRequestsQuerySchema.safeParse(query)
    if (!parsed.success) {
      throw new BadRequestException(z.treeifyError(parsed.error))
    }

    return this.communitiesService.listJoinRequests(id, parsed.data.status, { id: user.sub } as unknown as any)
  }

  @UseGuards(JwtAuthGuard)
  @Patch(':id/requests/:request_id')
  actOnRequest(
    @Param('id') id: string,
    @Param('request_id') requestId: string,
    @Body() body: any,
    @CurrentUser() user: JwtPayloadUser,
  ) {
    const parsed = CommunityRequestActionSchema.safeParse(body)
    if (!parsed.success) {
      throw new BadRequestException(z.treeifyError(parsed.error))
    }

    return this.communitiesService.actOnJoinRequest(id, requestId, parsed.data.action, { id: user.sub } as unknown as any)
  }

  @UseGuards(JwtAuthGuard)
  @Patch(':id/owner')
  transferOwner(
    @Param('id') id: string,
    @Body() body: any,
    @CurrentUser() user: JwtPayloadUser,
  ) {
    const parsed = TransferOwnerSchema.safeParse(body)
    if (!parsed.success) {
      throw new BadRequestException(z.treeifyError(parsed.error))
    }

    return this.communitiesService.transferOwnership(id, parsed.data.new_owner_user_id, { id: user.sub } as unknown as any)
  }
}
