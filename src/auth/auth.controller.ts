/* eslint-disable @typescript-eslint/no-unsafe-argument */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */
/* eslint-disable @typescript-eslint/require-await */
import { Body, Controller, Get, Post, Req, UseGuards } from '@nestjs/common';
import { AuthService } from './auth.service';
import { GoogleAuthGuard } from './guards/google-auth/google-auth.guard';
import { JwtAuthGuard } from './guards/google-auth/jwt-auth.guard';
import { SelectRoleDto } from './dto/select-role.dto';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Get('google')
  @UseGuards(GoogleAuthGuard)
  async googleAuth() {
    return;
  }

  @Get('google/callback')
  @UseGuards(GoogleAuthGuard)
  async googleCallback(@Req() req) {
    return this.authService.handleGoogleOauth(req.user);
  }

  @Post('onboarding/role')
  @UseGuards(JwtAuthGuard)
  selectRole(@Req() req, @Body() dto: SelectRoleDto) {
    return this.authService.setUserRole(req.user.userId, dto.role);
  }
}
