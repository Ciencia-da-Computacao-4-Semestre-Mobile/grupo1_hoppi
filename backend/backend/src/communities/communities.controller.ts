import { Body, Controller, Delete, Get, Param, Patch, Post, Req, UseGuards } from '@nestjs/common'
import { CommunitiesService } from './communities.service';
import { Community } from './communities.entity';
import { AuthGuard } from 'src/auth/guards/auth.guard';
import { CreateCommunityDto } from './dto/create-community.dto';

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
  create(@Body() dto: CreateCommunityDto, @Req() req) {
    return this.communitiesService.create(dto, { id: req.user.sub } as any);
  }

  @UseGuards(AuthGuard)
  @Patch(':id')
  update(@Param('id') id: string, @Body() dto: Partial<CreateCommunityDto>, @Req() req) {
    return this.communitiesService.update(id, dto, { id: req.user.sub } as any);
  }

  @UseGuards(AuthGuard)
  @Post(':id/join')
  join(@Param('id') id: string, @Req() req) {
    return this.communitiesService.join(id, { id: req.user.sub } as any);
  }

  @UseGuards(AuthGuard)
  @Delete(':id/leave')
  leave(@Param('id') id: string, @Req() req) {
    return this.communitiesService.leave(id, { id: req.user.sub } as any);
  }

  @UseGuards(AuthGuard)
  @Patch(':community/members/:user_id')
  updateMemberRole(
    @Param('community') communityId: string,
    @Param('user_id') userId: string,
    @Body('role') role: 'member' | 'moderator' | 'owner',
    @Req() req
  ) {
    return this.communitiesService.updateMemberRole(communityId, userId, role, { id: req.user.sub } as any);
  }
}
