/* eslint-disable @typescript-eslint/no-unsafe-member-access */
import { Body, Controller, Post, Req, UseGuards } from '@nestjs/common';
import { OnboardingService } from './onboarding.service';
import { SelectRoleDto } from './dto/select-role.dto';
import { JwtAuthGuard } from 'src/auth/guards/google-auth/jwt-auth.guard';

@Controller('onboarding')
export class OnboardingController {
  constructor(private onboardingService: OnboardingService) {}

  @Post('role')
  @UseGuards(JwtAuthGuard)
  setRole(@Req() req, @Body() dto: SelectRoleDto) {
    return this.onboardingService.setRole(req.user.userId, dto.role);
  }
}
