/* eslint-disable @typescript-eslint/no-unsafe-enum-comparison */
import { BadRequestException, Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { DoctorProfile } from './doctor.schema';
import { User, UserStatus } from '../../auth/schemas/user.schema';
import { CreateDoctorProfileDto } from './dto/create-doctor-profile.dto';

@Injectable()
export class DoctorService {
  constructor(
    @InjectModel(DoctorProfile.name)
    private doctorModel: Model<DoctorProfile>,
    @InjectModel(User.name)
    private userModel: Model<User>,
  ) {}

  async createProfile(userId: string, data: CreateDoctorProfileDto) {
    const user = await this.userModel.findById(userId);
    if (!user) throw new BadRequestException('User not found');

    if (user.role !== 'doctor') throw new BadRequestException('Not a doctor');

    const exists = await this.doctorModel.findOne({ userId });
    if (exists) throw new BadRequestException('Profile already exists');

    const profile = await this.doctorModel.create({
      userId,
      ...data,
    });

    user.status = UserStatus.UNDER_REVIEW;
    await user.save();

    return profile;
  }
}
