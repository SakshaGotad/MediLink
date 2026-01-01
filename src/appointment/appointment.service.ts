/* eslint-disable prettier/prettier */
import {  BadRequestException, Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Appointment, AppointmentStatus } from './schema/appointment.schema';
import { Model } from 'mongoose';
import { DoctorAvailability } from 'src/availability/availability.schema';
import { User, UserRole } from 'src/auth/schemas/user.schema';
import { CreateAppointmentDto } from './dto/create-appointment.dto';

@Injectable()
export class AppointmentService {
  constructor(
    @InjectModel(Appointment.name)
    private appointmentModel: Model<Appointment>,
    @InjectModel(DoctorAvailability.name)
    private availabilityModel: Model<DoctorAvailability>,
    @InjectModel(User.name)
    private userModel: Model<User>,
  ) {}

 private getDay(date: string) {
    return new Date(date)
      .toLocaleDateString('en-US', { weekday: 'short' })
      .toUpperCase();
  }

  async createAppointment(
    patientId: string,
    dto:CreateAppointmentDto
  ){
    const doctor = await this.userModel.findById(dto.doctorId);
    if(!doctor || doctor.role !== UserRole.DOCTOR){
        throw new BadRequestException('Invalid doctor');
    }
    const availability = await this.availabilityModel.findOne({
      doctorId: dto.doctorId,
      day: this.getDay(dto.date),
      isActive: true,
    });
     if (!availability){
      throw new BadRequestException('Doctor not available');
    }
     const count = await this.appointmentModel.countDocuments({
      doctorId: dto.doctorId,
      date: dto.date,
      startTime: dto.startTime,
      endTime: dto.endTime,
      status: AppointmentStatus.BOOKED,
    });
    if (count >= availability.maxPatientsPerSlot) {
      throw new BadRequestException('No slots available');
    }

    return this.appointmentModel.create({
      doctorId: dto.doctorId,
      patientId,
      date: dto.date,
      startTime: dto.startTime,
      endTime: dto.endTime,
    });

    
  }

  
}


