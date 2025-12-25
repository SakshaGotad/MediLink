/* eslint-disable @typescript-eslint/no-unsafe-return */
/* eslint-disable @typescript-eslint/no-unsafe-call */
/* eslint-disable prettier/prettier */
import { Body, Controller, Post } from '@nestjs/common';
import { AuthService } from './auth.service';
import { SignupDto } from './dto/signup.dto';
import { GoogleOauthDto } from './dto/google_oauth.dto';

@Controller('auth')
export class AuthController { 
    constructor(private readonly authService: AuthService) {}

    @Post('signup')
   signup(@Body() signupDto:SignupDto) {
        return this.authService.signup(signupDto);
    }

    @Post('oauth/google')
    googleOauth(@Body() dto:GoogleOauthDto){
        return this.authService.googleOauth(dto);
    }
}
