import { z } from 'zod'
import * as bcrypt from 'bcryptjs'
import { BadRequestException, Injectable, NotFoundException } from '@nestjs/common'
import { InjectRepository } from '@nestjs/typeorm'
import { IsNull, Repository } from 'typeorm'
import { User } from './users.entity'
import { CreateUserDTO, CreateUserSchema, mapToPublicUserDTO, UpdatePasswordDTO, UpdatePasswordSchema, UpdateUserDTO, UpdateUserSchema } from './schemas/user.schema'
import { Post } from '../posts/posts.entity'
import { SanitizedPost, sanitizePostsArray } from 'src/common/utils/post-sanitizer.util'

@Injectable()
export class UsersService {
  constructor(
    @InjectRepository(User)
    private readonly usersRepository: Repository<User>,

    @InjectRepository(Post)
    private readonly postsRepository: Repository<Post>
  ) { }

  async create(rawData: any) {
    const parsed = CreateUserSchema.safeParse(rawData)
    if (!parsed.success) {
      throw new BadRequestException(z.treeifyError(parsed.error))
    }

    const data: CreateUserDTO = parsed.data

    const exists = await this.usersRepository.findOne({
      where: [{ email: data.email }, { username: data.username }]
    })
    if (exists) {
      throw new BadRequestException('E-mail or username already in use.')
    }

    const passwordHash = await bcrypt.hash(data.password, 10)

    const user = this.usersRepository.create({
      ...data,
      password_hash: passwordHash,
      birth_date: new Date(data.birth_date)
    })

    await this.usersRepository.save(user)
    return { id: user.id, username: user.username }
  }

  async getByID(id: string) {
    const user = await this.usersRepository.findOne({
      where: { id }
    })
    if (!user) {
      throw new NotFoundException('User not found.')
    }

    return user
  }

  async getByUsername(username: string) {
    const user = await this.usersRepository.findOne({
      where: { username },
      select: ['id', 'username', 'display_name', 'avatar_key', 'institution']
    })
    if (!user) {
      throw new NotFoundException('User not found.')
    }
  }

  async update(id: string, rawData: any) {
    const parsed = UpdateUserSchema.safeParse(rawData)
    if (!parsed.success) {
      throw new BadRequestException(z.treeifyError(parsed.error))
    }

    const data: UpdateUserDTO = parsed.data

    await this.usersRepository.update(id, data)
    return this.getByID(id)
  }

  async updatePassword(id: string, rawData: any) {
    const parsed = UpdatePasswordSchema.safeParse(rawData)
    if (!parsed.success) {
      throw new BadRequestException(z.treeifyError(parsed.error))
    }

    const data: UpdatePasswordDTO = parsed.data

    const user = await this.usersRepository.findOne({
      where: { id }
    })
    if (!user) {
      throw new NotFoundException('User not found')
    }

    const valid = await bcrypt.compare(
      data.current_password,
      user.password_hash
    )
    if (!valid) {
      throw new BadRequestException('Current password incorrect.')
    }

    user.password_hash = await bcrypt.hash(data.new_password, 10)
    await this.usersRepository.save(user)

    return { message: 'Password updated sucessfully.' }
  }

  async suspend(id: string) {
    await this.usersRepository.update(id, { is_suspended: true })
    return { message: 'User suspended.' }
  }

  async getPostsByUser(userID: string): Promise<SanitizedPost[]> {
    const posts = await this.postsRepository.find({
        where: { 
            author: { id: userID },
            is_reply_to: IsNull()
        },
        relations: [
            'author', 
            'likes', 
            'replies', 
            'replies.author',
            'postCount'
        ], 
        order: { 
            created_at: 'DESC'
        }
    })

    return sanitizePostsArray(posts);
  }

  //Posts de comunidades dele
  // async getUserCommunityPosts(userID: string) {
  //   return this.communityPostsRepository.find({
  //     where: { user: { id: userId } },
  //     relations: ['user', 'community'],
  //     order: { created_at: 'DESC' },
  //   });
  // }

  async getUserLikes(userID: string) {
    return this.postsRepository.find({
      where: { likes: { user: { id: userID } } },
      order: { created_at: 'DESC' }
    })
  }
}
