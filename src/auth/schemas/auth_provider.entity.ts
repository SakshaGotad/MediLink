/* eslint-disable prettier/prettier */
import {
  Column,
  CreateDateColumn,
  Entity,
  Index,
  JoinColumn,
  ManyToOne,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { User } from './auth.entity';

export enum AuthProviderType {
  EMAIL = 'EMAIL',
  GOOGLE = 'GOOGLE',
}

@Entity({ name: 'auth_providers' })
@Index(['provider', 'provider_user_id'], { unique: true })
export class AuthProvider {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ type: 'uuid' })
  user_id: string;

  @ManyToOne(() => User, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'user_id' })
  user: User;

  @Column({
    type: 'enum',
    enum: AuthProviderType,
  })
  provider: AuthProviderType;

  @Column({ type: 'varchar', length: 255 })
  provider_user_id: string;

  @Column({ type: 'varchar', nullable: true })
  password_hash?: string;

  @CreateDateColumn({ type: 'timestamptz' })
  created_at: Date;
}
