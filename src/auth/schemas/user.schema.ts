import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

/* ======================
   ENUMS
====================== */

export enum UserRole {
  DOCTOR = 'doctor',
  PATIENT = 'patient',
}

export enum UserStatus {
  ONBOARDING = 'ONBOARDING',
  ACTIVE = 'ACTIVE',
  UNDER_REVIEW = 'UNDER_REVIEW',
  REJECTED = 'REJECTED',
}

/* ======================
   USER SCHEMA
====================== */

@Schema({ timestamps: true })
export class User extends Document {
  @Prop({ required: true, unique: true })
  email: string;

  @Prop({ required: true, unique: true })
  googleId: string;

  @Prop({ type: String, enum: UserRole, default: null })
  role: UserRole | null;

  @Prop({
    type: String,
    enum: UserStatus,
    default: UserStatus.ONBOARDING,
  })
  status: UserStatus;
}

export const UserSchema = SchemaFactory.createForClass(User);
