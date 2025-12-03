import 'reflect-metadata'
import { DataSource } from 'typeorm'
import { User } from '../users/users.entity'
import { Post } from '../posts/posts.entity'
import { Follow } from '../follows/follows.entity'
import { Like } from '../likes/likes.entity'
import { PostCount } from '../post_counts/post_counts.entity'
import { Community } from '../communities/communities.entity'
import { CommunityMember } from '../community_members/community_members.entity'
import { CommunityJoinRequest } from '../communities/entities/community-join-request.entity'
import { PasswordReset } from '../auth/entities/password-reset.entity'
import { env } from 'src/config/env'

export const AppDataSource = new DataSource({
  type: 'postgres',
  url: env.DATABASE_URL,
  entities: [User, Post, Follow, Like, PostCount, Community, CommunityMember, CommunityJoinRequest, PasswordReset],
  synchronize: true, // true só em dev! nunca em produção
  ssl: {
    rejectUnauthorized: false,
  } 
});