import { Controller, Get } from '@nestjs/common'
import { CommunitiesService } from './communities.service';
import { Community } from './communities.entity';

@Controller('communities')
export class CommunitiesController {
  constructor(private readonly communitiesService: CommunitiesService) {}

  @Get()
  findAll(): Promise<Community[]> {
    return this.communitiesService.findAll();
  }
}
