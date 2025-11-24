import { forwardRef, Module } from '@nestjs/common'
import { TypeOrmModule } from '@nestjs/typeorm'
import { FollowsService } from './follows.service'
import { Follow } from './follows.entity'
import { UsersModule } from 'src/users/users.module'

@Module({
  imports: [
    TypeOrmModule.forFeature([Follow]),
    forwardRef(() => UsersModule),
  ], 
  providers: [FollowsService],
  exports: [FollowsService], 
})
export class FollowsModule {}