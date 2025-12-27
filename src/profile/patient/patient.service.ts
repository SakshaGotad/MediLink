/* eslint-disable @typescript-eslint/no-unsafe-enum-comparison */
import { BadRequestException, Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { PatientProfile } from './patient.schema';
import { User, UserStatus } from '../../auth/schemas/user.schema';
import { CreatePatientProfileDto } from './dto/create-patient-profile.dto';

@Injectable()
export class PatientService {
  constructor(
    @InjectModel(PatientProfile.name)
    private patientModel: Model<PatientProfile>,
    @InjectModel(User.name)
    private userModel: Model<User>,
  ) {}

  async createProfile(userId: string, data: CreatePatientProfileDto) {
    const user = await this.userModel.findById(userId);
    if (!user) throw new BadRequestException('User not found');

    if (user.role !== 'patient') throw new BadRequestException('Not a patient');

    const exists = await this.patientModel.findOne({ userId });
    if (exists) throw new BadRequestException('Profile already exists');

    const profile = await this.patientModel.create({
      userId,
      ...data,
    });

    user.status = UserStatus.ACTIVE;
    await user.save();

    return profile;
  }
}
