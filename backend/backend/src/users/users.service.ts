import { Injectable } from '@nestjs/common'
import { InjectRepository } from '@nestjs/typeorm'
import { Repository } from 'typeorm'
import { User } from './users.entity'
import { PostsService } from 'src/posts/posts.service';

@Injectable()
export class UsersService {
  constructor(
    @InjectRepository(User)
    private usersRepository: Repository<User>,

    private postsService: PostsService,
  ) {}

  findAll() {
    return this.usersRepository.find();
  }

  async getUserPosts(userId: string) {
    return this.postsService.getPostsByUser(userId);
  }

}
