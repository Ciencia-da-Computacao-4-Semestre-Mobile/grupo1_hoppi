import { Column, CreateDateColumn, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn } from 'typeorm'
import { User } from 'src/users/users.entity'
import { Community } from 'src/communities/communities.entity'

@Entity('communityMembers')
export class CommunityMember {
    @PrimaryGeneratedColumn('uuid')
    id: string

    @ManyToOne(() => Community, (community) => community.members, { onDelete: "CASCADE" })
    @JoinColumn({ name: 'community_id' })
    community: Community

    @ManyToOne(() => User, user => user.communities, { onDelete: "CASCADE" })
    @JoinColumn({ name: 'user_id' })
    user: User

    @Column({ type: 'enum', enum: ['member', 'owner', 'moderator'], default: 'member' })
    role: string

    @CreateDateColumn()
    joined_at: Date
}