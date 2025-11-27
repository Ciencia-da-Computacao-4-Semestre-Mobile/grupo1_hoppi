import { Column, Entity, JoinColumn, OneToOne, PrimaryColumn } from 'typeorm'
import { Post } from 'src/posts/posts.entity'

@Entity('postCounts')
export class PostCount {
    @PrimaryColumn('uuid')
    post_id: string

    @OneToOne(() => Post, (post) => post.postCount, { onDelete: "CASCADE" })
    @JoinColumn({ name: 'post_id' })
    post: Post

    @Column({ default: 0 })
    reply_count: number

    @Column({ default: 0 })
    like_count: number
}