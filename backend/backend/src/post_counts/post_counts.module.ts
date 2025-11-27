import { Module } from '@nestjs/common'
import { TypeOrmModule } from '@nestjs/typeorm'
import { PostCountsService } from './post_counts.service'
import { PostCount } from './post_counts.entity'

@Module({
  imports: [TypeOrmModule.forFeature([PostCount])],
  providers: [PostCountsService],
  exports: [PostCountsService], 
})
export class PostCountsModule {}