import { BadRequestException, Injectable, NotFoundException } from '@nestjs/common'
import { InjectRepository } from '@nestjs/typeorm'
import { Repository } from 'typeorm'
import { Follow } from './follows.entity'
import { UsersService } from 'src/users/users.service'

@Injectable()
export class FollowsService {
  constructor(
    @InjectRepository(Follow)
    private followsRepository: Repository<Follow>,
    private usersService: UsersService
  ) { }

  async followUser(
    followerID: string,
    followeeID: string
  ) {
    if (followerID === followeeID) {
      throw new BadRequestException('You cannot follow yourself.')
    }

    // sim, está com erro, eu sei
    const followee = await this.usersService.getByID(followeeID)
    if (!followee) throw new NotFoundException('User not found.')

    const exists = await this.followsRepository.findOne({
      where: { follower_id: followerID, followee_id: followeeID }
    })
    if (exists) {
      throw new BadRequestException('You are already following this user.')
    }

    const follow = this.followsRepository.create({
      follower_id: followerID,
      followee_id: followeeID
    })

    return this.followsRepository.save(follow)
  }

  getFollowing(userID: string) {
    return this.followsRepository.find({
      where: { follower_id: userID },
      relations: ['followee']
    })
  }

  getFollowers(userID: string) {
    return this.followsRepository.find({
      where: { followee_id: userID },
      relations: ['follower']
    })
  }

  async unfollowUser(
    followerID: string,
    followeeID: string
  ) {
    const relation = await this.followsRepository.findOne({
      where: { follower_id: followerID, followee_id: followeeID }
    })
    if (!relation) {
      throw new NotFoundException('You are not following this user.')
    }

    await this.followsRepository.remove(relation)
    return { message: 'Unfollow action finished sucessfully.' }
  }

  async getFollowingIds(userID: string): Promise<string[]> {
    const follows = await this.followsRepository.find({
      where: { follower_id: userID },
      select: ['followee_id']
    })

    return follows.map(follow => follow.followee_id)
  }
}