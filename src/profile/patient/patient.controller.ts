/* eslint-disable prettier/prettier */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */
import { Body, Controller, Post, Req, UseGuards } from "@nestjs/common";
import { JwtAuthGuard } from "src/auth/guards/google-auth/jwt-auth.guard";
import { PatientService } from "./patient.service";
import { CreatePatientProfileDto } from "./dto/create-patient-profile.dto";
import { Throttle } from "@nestjs/throttler";

@Controller('patient')
@UseGuards(JwtAuthGuard)
export class PatientController {
  constructor(private service: PatientService) {}

   @Throttle({ default: { limit: 5, ttl: 60 } })
  @Post('profile')
  create(@Req() req, @Body() body: CreatePatientProfileDto) {
    return this.service.createProfile(req.user.userId, body);
  }
}
