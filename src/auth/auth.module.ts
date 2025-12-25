/* eslint-disable prettier/prettier */
import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { JwtModule } from '@nestjs/jwt';
import { PassportModule } from '@nestjs/passport';

import { AuthController } from './auth.controller';
import { AuthService } from './auth.service';
import { GoogleStrategy } from './strategies/google.strategy';
import { JwtStrategy } from './strategies/jwt.strategy';
import { GoogleAuthGuard } from './guards/google-auth/google-auth.guard';

import googleOauthconfig from '../config/google-oauth.config';
import jwtConfig from '../config/jwt.config';
import { TypeOrmModule } from '@nestjs/typeorm';
import { User } from './schemas/auth.entity';
import { AuthProvider } from './schemas/auth_provider.entity';

@Module({
  imports: [
    // Load configs
    ConfigModule.forFeature(googleOauthconfig),
    ConfigModule.forFeature(jwtConfig),
    TypeOrmModule.forFeature([User, AuthProvider]),

    PassportModule,
    JwtModule.registerAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: (configService: ConfigService) => ({
        secret: configService.getOrThrow<string>('jwt.secret'),
        signOptions: {
          expiresIn: parseInt(configService.getOrThrow<string>('jwt.expiresIn'), 10),
        },
      }),
    }),
  ],

  controllers: [AuthController],
  providers: [
    AuthService,
    GoogleStrategy,
    JwtStrategy,
    GoogleAuthGuard,
  ],

  exports: [JwtModule],
})
export class AuthModule {}
