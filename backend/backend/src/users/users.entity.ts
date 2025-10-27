import { Column, CreateDateColumn, Entity, OneToMany, PrimaryGeneratedColumn } from 'typeorm'
import { Like } from 'src/likes/likes.entity'
import { CommunityMember } from 'src/community_members/community_members.entity'
import { Post } from 'src/posts/posts.entity'
import { Follow } from 'src/follows/follows.entity'

@Entity('users')
export class User {
    @PrimaryGeneratedColumn('uuid')
    id: string

    @Column({ unique: true })
    username: string

    @Column()
    display_name: string

    @Column({ default: 'avatar_1' })
    avatar_key: string;

    @Column({ unique: true })
    email: string

    @Column({ type: 'date' })
    birth_date: Date

    @Column()
    institution: string

    @Column()
    password_hash: string

    @CreateDateColumn()
    created_at: Date

    @Column({ default: false })
    is_suspended: boolean

    @OneToMany(() => Post, post => post.author)
    posts: Post[]

    @OneToMany(() => Follow, follow => follow.follower)
    following: Follow[]

    @OneToMany(() => Follow, follow => follow.followee)
    followers: Follow[]

    @OneToMany(() => Like, like => like.user)
    likes: Like[]

    @OneToMany(() => CommunityMember, member => member.user)
    communities: CommunityMember[]
}