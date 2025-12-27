/* eslint-disable prettier/prettier */
import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Types, Document } from 'mongoose';
import { randomUidPlugin } from 'src/common/plugins/mongo-random-uid.plugin';

export enum DoctorVerificationStatus {
  PENDING = 'PENDING',
  VERIFIED = 'VERIFIED',
  REJECTED = 'REJECTED',
}

@Schema({ timestamps: true })
export class DoctorProfile extends Document {
  @Prop({ unique: true })
  uid: string;

  @Prop({ type: Types.ObjectId, ref: 'User', required: true })
  userId: Types.ObjectId;

  @Prop({ required: true })
  fullName: string;

  @Prop({ required: true })
  specialization: string;

  @Prop({ required: true })
  experienceYears: number;

  @Prop({ required: true })
  licenseNumber: string;

  @Prop({ default: false })
  isVerified: boolean;

  @Prop({
    enum: DoctorVerificationStatus,
    default: DoctorVerificationStatus.PENDING,
  })
  verificationStatus: DoctorVerificationStatus;
}

export const DoctorProfileSchema = SchemaFactory.createForClass(DoctorProfile);

// APPLY PLUGIN
DoctorProfileSchema.plugin(randomUidPlugin('DOC'));
