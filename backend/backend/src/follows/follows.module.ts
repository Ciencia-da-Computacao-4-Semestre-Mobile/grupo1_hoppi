import { Module } from '@nestjs/common'
import { TypeOrmModule } from '@nestjs/typeorm'
import { FollowsService } from './follows.service'
import { Follow } from './follows.entity'

@Module({
  imports: [TypeOrmModule.forFeature([Follow])],
  providers: [FollowsService],
  exports: [FollowsService], 
})
export class FollowsModule {}