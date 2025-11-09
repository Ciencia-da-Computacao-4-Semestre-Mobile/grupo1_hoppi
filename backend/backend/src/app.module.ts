import { Module } from '@nestjs/common'
import { TypeOrmModule } from '@nestjs/typeorm'
import { ConfigModule } from '@nestjs/config'
import { AppDataSource } from './database/db'
import { UsersModule } from './users/users.module'
import { PostsModule } from './posts/posts.module'
import { FollowsModule } from './follows/follows.module'
import { LikesModule } from './likes/likes.module'
import { PostCountsModule } from './post_counts/post_counts.module'
import { CommunitiesModule } from './communities/communities.module'
import { CommunityMembersModule } from './community_members/community_members.module'
import { AuthModule } from './auth/auth.module'
import { MailModule } from './nodemailer/mailer.module'

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
    }),
    TypeOrmModule.forRoot(AppDataSource.options),
    UsersModule,
    PostsModule,
    FollowsModule,
    LikesModule,
    PostCountsModule,
    CommunitiesModule,
    CommunityMembersModule,
    AuthModule,
  ],
})

export class AppModule {}