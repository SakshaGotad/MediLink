import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { DoctorProfile, DoctorProfileSchema } from './doctor/doctor.schema';
import { PatientProfile, PatientProfileSchema } from './patient/patient.schema';
import { DoctorController } from './doctor/doctor.controller';
import { PatientController } from './patient/patient.controller';
import { DoctorService } from './doctor/doctor.service';
import { PatientService } from './patient/patient.service';
import { User, UserSchema } from 'src/auth/schemas/user.schema';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: DoctorProfile.name, schema: DoctorProfileSchema },
      { name: PatientProfile.name, schema: PatientProfileSchema },
      { name: User.name, schema: UserSchema },
    ]),
  ],
  controllers: [DoctorController, PatientController],
  providers: [DoctorService, PatientService],
})
export class ProfileModule {}
