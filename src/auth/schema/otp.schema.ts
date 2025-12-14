/* eslint-disable prettier/prettier */
import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";
import { Document } from "mongoose";



@Schema({timestamps: true})
export class OTP{
    @Prop({required: true})
    email: string;

    @Prop({required: true})
    otp: string;

    @Prop({default: false})
    isUsed: boolean;

    @Prop({required: true})
    expiresAt: Date;

}
export type OTPDocument = OTP & Document;
export const OTPSchema = SchemaFactory.createForClass(OTP);