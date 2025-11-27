import { forwardRef, Module } from '@nestjs/common'
import { TypeOrmModule } from '@nestjs/typeorm'
import { FollowsService } from './follows.service'
import { Follow } from './follows.entity'
import { UsersModule } from 'src/users/users.module'
import { FollowController } from './follows.controller'

@Module({
  imports: [
    TypeOrmModule.forFeature([Follow]),
    forwardRef(() => UsersModule),
  ], 
  providers: [FollowsService],
  controllers: [FollowController],
  exports: [FollowsService], 
})
export class FollowsModule {}