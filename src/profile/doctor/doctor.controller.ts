/* eslint-disable prettier/prettier */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */
import { Body, Controller, Post, Req, UseGuards } from "@nestjs/common";
import { JwtAuthGuard } from "src/auth/guards/google-auth/jwt-auth.guard";
import { DoctorService } from "./doctor.service";
import { CreateDoctorProfileDto } from "./dto/create-doctor-profile.dto";
import { Throttle } from "@nestjs/throttler";

@Controller('doctor')
@UseGuards(JwtAuthGuard)
export class DoctorController {
  constructor(private service: DoctorService) {}
 
   @Throttle({ default: { limit: 5, ttl: 60 } })
  @Post('profile')
  create(@Req() req, @Body() body: CreateDoctorProfileDto) {
    return this.service.createProfile(req.user.userId, body);
  }
}
