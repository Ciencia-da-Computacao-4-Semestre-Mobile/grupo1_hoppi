import { CreateDateColumn, Entity, JoinColumn, ManyToOne, PrimaryColumn } from 'typeorm'
import { User } from 'src/users/users.entity'
import { Post } from 'src/posts/posts.entity'

@Entity('likes')
export class Like {
    @PrimaryColumn('uuid')
    user_id: string

    @PrimaryColumn('uuid')
    post_id: string

    @ManyToOne(() => User, user => user.likes, { onDelete: "CASCADE" })
    @JoinColumn({ name: 'user_id' })
    user: User

    @ManyToOne(() => Post, (post) => post.likes, { onDelete: "CASCADE" })
    @JoinColumn({ name: 'post_id' })
    post: Post

    @CreateDateColumn()
    liked_at: Date
}