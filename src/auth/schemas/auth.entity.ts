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

  @Column({ type: 'varchar', length: 255, nullable: true })
  password: string | null;

  @Column({ type: 'enum', enum: UserRole, nullable: true })
  role: UserRole;

  @Column({ type: 'enum', enum: UserStatus , nullable: true })
  state: UserStatus;

  @CreateDateColumn({ type: 'timestamptz', nullable: true })
  created_at: Date;
 
  @UpdateDateColumn({ type: 'timestamptz' , nullable: true })
  updated_at: Date;
}

  @Index({ unique: true })
  @Column({ type: 'varchar', length: 255 })
  email: string;

  @Column({ type: 'varchar', length: 255, nullable: true })
  password: string | null;

  @Column({ type: 'enum', enum: UserRole, nullable: true })
  role: UserRole;

  @Column({ type: 'enum', enum: UserStatus , nullable: true })
  state: UserStatus;

  @CreateDateColumn({ type: 'timestamptz', nullable: true })
  created_at: Date;
 
  @UpdateDateColumn({ type: 'timestamptz' , nullable: true })
  updated_at: Date;
}
