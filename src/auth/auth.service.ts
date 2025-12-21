/* eslint-disable prettier/prettier */
import { Injectable } from '@nestjs/common';
import { UsersService } from 'src/users/users.service';
import { OTPDocument } from './schema/otp.schema';
import { Model } from 'mongoose';

@Injectable()
export class AuthService {
    constructor(
        private readonly usersService: UsersService,
        private readonly otpModel: Model<OTPDocument>
    ) {}
}
