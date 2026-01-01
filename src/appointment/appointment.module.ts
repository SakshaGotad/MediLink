/* eslint-disable prettier/prettier */
import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { AppointmentService } from './appointment.service';
import { AppointmentController } from './appointment.controller';
import { User, UserSchema } from '../auth/schemas/user.schema';
import { Appointment, AppointmentSchema } from './schema/appointment.schema';
import { DoctorAvailability, DoctorAvailabilitySchema } from 'src/availability/availability.schema';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: Appointment.name, schema: AppointmentSchema },
      { name: DoctorAvailability.name, schema: DoctorAvailabilitySchema },
      { name: User.name, schema: UserSchema },
    ]),
  ],
  controllers: [AppointmentController],
  providers: [AppointmentService],
})
export class AppointmentModule {}
