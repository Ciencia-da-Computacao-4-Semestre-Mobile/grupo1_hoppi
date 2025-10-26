import { Column, CreateDateColumn, Entity, JoinColumn, ManyToOne, OneToMany, PrimaryGeneratedColumn } from 'typeorm'
import { CommunityMember } from 'src/community_members/community_members.entity'
import { User } from 'src/users/users.entity'

@Entity('communities')
export class Community {
    @PrimaryGeneratedColumn('uuid')
    id: string

    @Column({ unique: true })
    name: string

    @Column({ type: 'text' })
    description: string

    @ManyToOne(() => User, { onDelete: 'SET NULL' })
    @JoinColumn({ name: 'created_by' })
    created_by: User

    @CreateDateColumn()
    created_at: Date

    @OneToMany(() => CommunityMember, (member) => member.community)
    members: CommunityMember[]
}