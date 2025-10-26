import 'reflect-metadata'
import { DataSource } from 'typeorm'
import { User } from '../users/users.entity'
import { Post } from '../posts/posts.entity'
import { Follow } from '../follows/follows.entity'
import { Like } from '../likes/likes.entity'
import { PostCount } from '../post_counts/post_counts.entity'
import { Community } from '../communities/communities.entity'
import { CommunityMember } from '../community_members/community_members.entity'
import { env } from 'src/config/env'

export const AppDataSource = new DataSource({
  type: 'postgres',
  host: env.DB_HOST,
  port: env.DB_PORT,
  username: env.DB_USERNAME,
  password: env.DB_PASSWORD,
  database: 'postgres',
  entities: [User, Post, Follow, Like, PostCount, Community, CommunityMember],
  synchronize: true, // true só em dev! nunca em produção
  ssl: true, 
});