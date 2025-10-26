import { Column, CreateDateColumn, Entity, JoinColumn, ManyToOne, OneToMany, OneToOne, PrimaryGeneratedColumn } from 'typeorm'
import { PostCount } from 'src/post_counts/post_counts.entity'
import { User } from 'src/users/users.entity'
import { Like } from 'src/likes/likes.entity'

@Entity('posts')
export class Post {
    @PrimaryGeneratedColumn('uuid')
    id: string

    @ManyToOne(() => User, user => user.posts, { onDelete: "CASCADE" })
    @JoinColumn({ name: 'author_id' })
    author: User

    @Column('text')
    content: string

    @CreateDateColumn()
    created_at: Date

    @ManyToOne(() => Post, (post) => post.replies, { nullable: true })
    @JoinColumn({ name: 'is_reply_to' })
    is_reply_to: Post

    @OneToMany(() => Post, (post) => post.is_reply_to)
    replies: Post[]

    @Column({ default: 0 })
    reply_count: number

    @Column({ default: false })
    is_deleted: boolean

    @Column({ type: 'jsonb', nullable: true })
    metadata: Record<string, any>

    @OneToMany(() => Like, (like) => like.post)
    likes: Like[]

    @OneToOne(() => PostCount, (count) => count.post)
    postCount: PostCount
}