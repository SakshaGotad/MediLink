/* eslint-disable prettier/prettier */
import { BadRequestException, ForbiddenException, Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { DoctorAvailability } from './availability.schema';
import { Model, Types } from 'mongoose';
import { User, UserRole, UserStatus } from 'src/auth/schemas/user.schema';
import { CreateAvailabilityDto } from './dto/create-availability.dto';

@Injectable()
export class AvailabilityService {
  constructor(
    @InjectModel(DoctorAvailability.name)
    private availabilityModel: Model<DoctorAvailability>,
    @InjectModel(User.name)
    private userModel: Model<User>,
  ) {}

  async createAvailability(doctorId: string, createDto: CreateAvailabilityDto) {
    const user = await this.userModel.findById(doctorId);
    if (!user) throw new Error('User not found');

    if (user.role !== UserRole.DOCTOR)
      throw new BadRequestException('Not a doctor');

    if (user.status !== UserStatus.ACTIVE)
      throw new BadRequestException('Doctor is not approved');

    const existing = await this.availabilityModel.findOne({
      doctorId: user._id,
      day: createDto.day,
    });
    if (existing) {
      throw new BadRequestException('Availability already exists for this day');
    }

    return this.availabilityModel.create({
      doctorId: user._id,
      ...createDto,
    });
  }

  async getDoctorAvailability(doctorId: string) {
  // 1️⃣ Validate ObjectId
  if (!Types.ObjectId.isValid(doctorId)) {
    throw new BadRequestException('Invalid doctor id');
  }

  // 2️⃣ Check doctor exists
  const doctor = await this.userModel.findById(doctorId);

  if (!doctor) {
    throw new NotFoundException('Doctor not found');
  }

  // 3️⃣ Ensure role is doctor
  if (doctor.role !== UserRole.DOCTOR) {
    throw new BadRequestException('User is not a doctor');
  }

  // 4️⃣ Ensure doctor is approved
  if (doctor.status !== UserStatus.ACTIVE) {
    throw new ForbiddenException('Doctor is not approved yet');
  }

  // 5️⃣ Fetch availability
  return this.availabilityModel.find({
    doctorId,
    isActive: true,
  });

  }
}
