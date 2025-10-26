import { Module } from '@nestjs/common'
import { TypeOrmModule } from '@nestjs/typeorm'
import { CommunitiesService } from './communities.service'
import { CommunitiesController } from './communities.controller'
import { Community } from './communities.entity'

@Module({
  imports: [TypeOrmModule.forFeature([Community])], 
  controllers: [CommunitiesController],              
  providers: [CommunitiesService]
})

export class CommunitiesModule {}