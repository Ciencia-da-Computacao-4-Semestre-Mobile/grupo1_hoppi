import { Injectable, NotFoundException } from '@nestjs/common'
import { InjectRepository } from '@nestjs/typeorm'
import { Repository } from 'typeorm'
import { Like } from './likes.entity'
import { Post } from 'src/posts/posts.entity'

@Injectable()
export class LikesService {
  constructor(
    @InjectRepository(Like)
    private likesRepository: Repository<Like>,

    @InjectRepository(Post)
    private postsRepository: Repository<Post>
  ) {}

  async likePost(userID: string, postID: string){
    const postExists = await this.postsRepository.findOne({
      where: { id: postID }
    })
    if(!postExists){
      throw new NotFoundException('Post not found.')
    }

    const likeExists = await this.likesRepository.findOne({
      where: {
        user_id: userID,
        post_id: postID
      }
    })
    if(likeExists) return likeExists

    const like = this.likesRepository.create({
      user_id: userID,
      post_id: postID
    })
    return this.likesRepository.save(like)
  }

  async unlikePost(userID: string, postID: string){
    const like = await this.likesRepository.findOne({
      where: {
        user_id: userID,
        post_id: postID
      }
    })
    if(!like) return

    return this.likesRepository.remove(like)
  }

  async getLikes(postID: string){
    return this.likesRepository.count({
      where: { post_id: postID }
    })
  }

}