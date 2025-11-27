import { Controller, Delete, Get, Param, Post, Req, UnauthorizedException, UseGuards } from '@nestjs/common'
import { LikesService } from './likes.service'
import { JwtAuthGuard } from 'src/auth/guards/jwt-auth.guard'
import type { AuthRequest } from 'src/auth/interfaces/auth-request.interface';

@Controller()
export class LikeController {
    constructor(
        private readonly likesService: LikesService
    ) {}

    @UseGuards(JwtAuthGuard)
    @Post('posts/:id/like')
    like(
        @Req() req: AuthRequest, 
        @Param('id') postID: string
    ) {
        if (!req.user) throw new UnauthorizedException()
        return this.likesService.likePost(req.user.sub, postID)
    }

    @UseGuards(JwtAuthGuard)
    @Delete('posts/:id/like')
    unlike(
        @Req() req: AuthRequest, 
        @Param('id') postID: string
    ) {
        if (!req.user) throw new UnauthorizedException()
        return this.likesService.unlikePost(req.user.sub, postID)
    }

    @Get('posts/:id/likes')
    getLikes(@Param('id') postID: string) {
        return this.likesService.getLikes(postID)
    }

}