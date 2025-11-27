import { Injectable } from '@nestjs/common'
import { InjectRepository } from '@nestjs/typeorm'
import { Repository } from 'typeorm'
import { PostCount } from './post_counts.entity'

@Injectable()
export class PostCountsService {
  constructor(
    @InjectRepository(PostCount)
    private postCountsRepository: Repository<PostCount>,
  ) {}

}
