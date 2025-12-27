/* eslint-disable prettier/prettier */
import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Types, Document } from 'mongoose';
import { randomUidPlugin } from 'src/common/plugins/mongo-random-uid.plugin';
export enum PatientGender {
  MALE = 'male',
  FEMALE = 'female',
  OTHER = 'other',
}

@Schema({ timestamps: true })
export class PatientProfile extends Document {
  @Prop({ type: Types.ObjectId, ref: 'User', required: true, unique: true })
  userId: Types.ObjectId;

  @Prop({ required: true })
  fullName: string;

  @Prop()
  age?: number;

  @Prop({
    enum: PatientGender,
  })
  gender?: PatientGender;

}

export const PatientProfileSchema = SchemaFactory.createForClass(PatientProfile);

PatientProfileSchema.plugin(randomUidPlugin('PAT'));