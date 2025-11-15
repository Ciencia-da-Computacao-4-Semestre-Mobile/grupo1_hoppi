import { Column, CreateDateColumn, Entity, JoinColumn, ManyToOne, OneToMany, PrimaryGeneratedColumn } from 'typeorm'
import { CommunityMember } from 'src/community_members/community_members.entity'
import { User } from 'src/users/users.entity'
import { Post } from 'src/posts/posts.entity'

@Entity('communities')
export class Community {
    @PrimaryGeneratedColumn('uuid')
    id: string

    @Column({ unique: true })
    name: string

    @Column({ default: 'avatar_1' })
    avatar: string

    @Column({ type: 'text' })
    description: string

    @Column({ default: false })
    is_private: boolean

    @Column({ default: false })
    requires_approval: boolean

    @Column({ default: 0 })
    member_count: number

    @ManyToOne(() => User, { onDelete: 'SET NULL' })
    @JoinColumn({ name: 'created_by' })
    created_by: User

    @CreateDateColumn()
    created_at: Date

    @OneToMany(() => CommunityMember, (member) => member.community)
    members: CommunityMember[]

    @OneToMany(() => Post, (post) => post.community) 
    posts: Post[]
}