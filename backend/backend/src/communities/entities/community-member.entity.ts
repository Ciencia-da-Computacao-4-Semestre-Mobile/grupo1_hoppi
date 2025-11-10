import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, CreateDateColumn } from 'typeorm';
import { Community } from '../communities.entity';
import { User } from '../../users/users.entity';

export enum MemberRole {
    OWNER = 'owner',
    MODERATOR = 'moderator',
    MEMBER = 'member'
}

@Entity()
export class CommunityMember {
    @PrimaryGeneratedColumn()
    id: number;

    @ManyToOne(() => Community)
    community: Community;

    @ManyToOne(() => User)
    user: User;

    @Column({
        type: 'enum',
        enum: MemberRole,
        default: MemberRole.MEMBER
    })
    role: MemberRole;

    @CreateDateColumn()
    joinedAt: Date;

    @Column({ default: true })
    isActive: boolean;
}