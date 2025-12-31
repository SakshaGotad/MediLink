import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document, Types } from 'mongoose';
import { randomUidPlugin } from 'src/common/plugins/mongo-random-uid.plugin';

export enum DayOfWeek {
  MON = 'MON',
  TUE = 'TUE',
  WED = 'WED',
  THU = 'THU',
  FRI = 'FRI',
  SAT = 'SAT',
  SUN = 'SUN',
}

@Schema({ timestamps: true })
export class DoctorAvailability extends Document {
  @Prop({ type: Types.ObjectId, ref: 'User', required: true })
  doctorId: Types.ObjectId;

  @Prop({ enum: DayOfWeek, required: true })
  day: DayOfWeek;

  @Prop({ required: true })
  startTime: string; // "09:00"

  @Prop({ required: true })
  endTime: string; // "17:00"

  @Prop({ default: 30 })
  slotDuration: number; // minutes

  @Prop({ default: 1 })
  maxPatientsPerSlot: number;

  @Prop({ default: true })
  isActive: boolean;
}

export const DoctorAvailabilitySchema =
  SchemaFactory.createForClass(DoctorAvailability);

DoctorAvailabilitySchema.plugin(randomUidPlugin('AVL'));
