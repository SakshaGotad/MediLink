/* eslint-disable @typescript-eslint/no-unsafe-member-access */
/* eslint-disable prettier/prettier */
import { Body, Controller, Post, UseGuards,Request, Get, Req } from '@nestjs/common';
import { JwtAuthGuard } from 'src/auth/guards/google-auth/jwt-auth.guard';
import { AvailabilityService } from './availability.service';
import { CreateAvailabilityDto } from './dto/create-availability.dto';
import { Throttle } from '@nestjs/throttler';

@Controller('availability')
@UseGuards(JwtAuthGuard)
export class AvailabilityController {
    constructor(private readonly availabilityService: AvailabilityService) {}

     @Throttle({ default: { limit: 10, ttl: 60 } })
    @Post()
    async createAvailability(@Request() req, @Body() createDto: CreateAvailabilityDto) {
        return this.availabilityService.createAvailability(req.user.userId, createDto);
    }

 @Throttle({ default: { limit: 60, ttl: 60 } })
  @Get(':doctorId')
  getDoctorAvailability(@Req() req) {
    return this.availabilityService.getDoctorAvailability(req.params.doctorId);
  }

}
