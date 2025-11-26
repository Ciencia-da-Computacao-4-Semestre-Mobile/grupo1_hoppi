// import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, CreateDateColumn } from 'typeorm';
// import { Community } from '../communities.entity';
// import { User } from '../../users/users.entity';

// @Entity()
// export class CommunityPost {
//     @PrimaryGeneratedColumn()
//     id: number;

//     @Column('text')
//     content: string;

//     @ManyToOne(() => Community, community => community.posts)
//     community: Community;

//     @ManyToOne(() => User, user => user.posts)
//     author: User;

//     @CreateDateColumn()
//     createdAt: Date;

//     @Column({ default: 0 })
//     likes: number;

//     @Column({ default: 0 })
//     comments: number;

//     @Column('simple-array', { nullable: true })
//     tags: string[];
// }