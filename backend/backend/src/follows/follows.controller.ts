import { z } from 'zod'
import { BadRequestException, Body, Controller, Delete, Get, Param, Post, Req, UseGuards } from '@nestjs/common'
import { FollowsService } from './follows.service'
import { AuthGuard } from '@nestjs/passport'
import { CreateFollowSchema, DeleteFollowSchema } from './schemas/follow.schema'
import type { AuthRequest } from '../auth/interfaces/auth-request.interface'

@Controller()
export class FollowController {
    constructor(
        private followService: FollowsService
    ){}

    @UseGuards(AuthGuard)
    @Post('follows')
    async follow(
        @Body() body: any,
        @Req() req: AuthRequest
    ){
        const parsed = CreateFollowSchema.safeParse(body)
        if(!parsed.success){
            throw new BadRequestException(z.treeifyError(parsed.error))
        }

        if (!req.user) {
            throw new BadRequestException('User not authenticated')
        }

        return this.followService.followUser(req.user.sub, parsed.data.followee_id)
    }

    @Get('users/:id/following')
    getFollowing(@Param('id') id: string) {
        return this.followService.getFollowing(id)
    }

    @Get('users/:id/followers')
    getFollowers(@Param('id') id: string) {
        return this.followService.getFollowers(id)
    }

    @UseGuards(AuthGuard)
    @Delete('follows/:followee_id')
    async unfollow(
        @Param('followee_id') followee_id: string, 
        @Req() req: AuthRequest
    ) {
        const parsed = DeleteFollowSchema.safeParse({ followee_id })
        if (!parsed.success) {
        throw new BadRequestException(z.treeifyError(parsed.error))
        }

        if (!req.user) {
            throw new BadRequestException('User not authenticated')
        }

        return this.followService.unfollowUser(req.user.sub, parsed.data.followee_id)
    }
}