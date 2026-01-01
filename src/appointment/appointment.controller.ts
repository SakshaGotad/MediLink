/* eslint-disable @typescript-eslint/no-unsafe-member-access */
import { Body, Controller, Post, Req, UseGuards } from '@nestjs/common';
import { AppointmentService } from './appointment.service';
import { CreateAppointmentDto } from './dto/create-appointment.dto';
import { JwtAuthGuard } from 'src/auth/guards/google-auth/jwt-auth.guard';
import { Throttle } from '@nestjs/throttler';

@Controller('appointments')
@UseGuards(JwtAuthGuard)
export class AppointmentController {
  constructor(private service: AppointmentService) {}

  @Throttle({ default: { limit: 5, ttl: 60 } })
  @Post()
  book(@Req() req, @Body() dto: CreateAppointmentDto) {
    return this.service.createAppointment(req.user.userId, dto);
  }
}
