/* eslint-disable prettier/prettier */
import { Injectable } from '@nestjs/common';
import { UsersService } from 'src/users/users.service';
import { Model } from 'mongoose';
import { OTPDocument } from './schemas/otp.schema';

@Injectable()
export class AuthService {
    constructor(
        private readonly usersService: UsersService,
        private readonly otpModel: Model<OTPDocument>
    ) {}
}
