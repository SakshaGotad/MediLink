/* eslint-disable prettier/prettier */
import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";
import { Document } from "mongoose";

export type UserDocument = Auth & Document;
@Schema({timestamps: true})
export class Auth {
    @Prop({required: true, unique: true})
    email: string;

    @Prop({required: true})
    password: string;

    @Prop({default:false})
    isVerified: boolean;
}

export const UserSchema = SchemaFactory.createForClass(Auth);