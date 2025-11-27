import { CreateDateColumn, Entity, JoinColumn, ManyToOne, PrimaryColumn } from 'typeorm'
import { User } from 'src/users/users.entity'

@Entity('follows')
export class Follow {
    @PrimaryColumn('uuid')
    follower_id: string

    @PrimaryColumn('uuid')
    followee_id: string

    @ManyToOne(() => User, (user) => user.following, { onDelete: "CASCADE" })
    @JoinColumn({ name: 'follower_id' })
    follower: User

    @ManyToOne(() => User, (user) => user.followers, { onDelete: "CASCADE" })
    @JoinColumn({ name: 'followee_id' })
    followee: User

    @CreateDateColumn()
    followed_at: Date

}