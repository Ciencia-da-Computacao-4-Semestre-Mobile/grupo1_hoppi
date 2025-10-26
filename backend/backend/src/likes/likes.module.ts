import { Module } from '@nestjs/common'
import { TypeOrmModule } from '@nestjs/typeorm'
import { LikesService } from './likes.service'
import { Like } from './likes.entity'

@Module({
  imports: [TypeOrmModule.forFeature([Like])],
  providers: [LikesService],
  exports: [LikesService], 
})
export class LikesModule {}