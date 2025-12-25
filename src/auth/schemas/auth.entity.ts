/* eslint-disable prettier/prettier */

import {
  Column,
  CreateDateColumn,
  Entity,
  Index,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from 'typeorm';

export enum UserRole {
  PATIENT = 'PATIENT',
  DOCTOR = 'DOCTOR',
}

export enum UserStatus {
  ONBOARDING = 'ONBOARDING',
  ACTIVE = 'ACTIVE',
  PENDING_VERIFICATION = 'PENDING_VERIFICATION',
  SUSPENDED = 'SUSPENDED',
}

@Entity({ name: 'users' })
export class User {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Index({ unique: true })
  @Column({ type: 'varchar', length: 255 })
  email: string;

  @Column({ type: 'varchar', length: 255 })
  password: string;

  @Column({ type: 'enum', enum: UserRole })
  role: UserRole;

  @Column({ type: 'enum', enum: UserStatus })
  state: UserStatus;

  @CreateDateColumn({ type: 'timestamptz' })
  created_at: Date;
 
  @UpdateDateColumn({ type: 'timestamptz' })
  updated_at: Date;
}
