/* eslint-disable prettier/prettier */
import { Module } from '@nestjs/common';
import { AuthController } from './auth.controller';
import { AuthService } from './auth.service';
import { ConfigModule } from '@nestjs/config';
import googleOauthconfig from '../config/google-oauth.config';
@Module({
 imports:[
  ConfigModule.forFeature(googleOauthconfig)
 ],
  controllers: [AuthController],
  providers: [AuthService],
})
export class AuthModule {}
