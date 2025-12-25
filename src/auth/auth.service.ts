/* eslint-disable @typescript-eslint/no-unsafe-assignment */
/* eslint-disable @typescript-eslint/no-unsafe-call */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */
import { BadRequestException, Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { User } from './schemas/auth.entity';
import { Repository } from 'typeorm';
import { AuthProvider, AuthProviderType } from './schemas/auth_provider.entity';
import { SignupDto } from './dto/signup.dto';
import { UserRole, UserStatus } from './schemas/auth.entity';
import * as bcrypt from 'bcrypt';
import { JwtService } from '@nestjs/jwt';
@Injectable()
export class AuthService {
  constructor(
    @InjectRepository(User)
    private readonly userRepository: Repository<User>,
    @InjectRepository(AuthProvider)
    private readonly authProviderRepository: Repository<AuthProvider>,
    private readonly jwtService: JwtService,
  ) {}

  async signup(dto: SignupDto) {
    const { email, password } = dto;

    const existingProvider = await this.authProviderRepository.findOne({
      where: {
        provider: AuthProviderType.EMAIL,
        provider_user_id: email,
      },
    });

    if (existingProvider) {
      throw new BadRequestException('Email already registered');
    }

    // Simplified and ensured safe handling of bcrypt.hash
    let passwordHash: string;
    try {
      passwordHash = await bcrypt.hash(password, 10);
    } catch {
      throw new Error('Error hashing password');
    }

    const user = this.userRepository.create({
      email: email,
      password: passwordHash,
      role: UserRole.PATIENT,
      state: UserStatus.ONBOARDING,
    });

    await this.userRepository.save(user);

    const authProvider = this.authProviderRepository.create({
      user_id: user.id,
      provider: AuthProviderType.EMAIL,
      provider_user_id: email,
      password_hash: passwordHash,
    });

    await this.authProviderRepository.save(authProvider);
    return {
      message: 'Signup successful',
      user_id: user.id,
    };
  }

  async handleGoogleOauth(oauthUser: {
    provider: string;
    provider_user_id: string;
    email: string;
  }) {
    let user = await this.userRepository.findOne({
      where: { email: oauthUser.email },
    });

    if (!user) {
      user = this.userRepository.create({
        email: oauthUser.email,
        state: UserStatus.ONBOARDING,
      });
      await this.userRepository.save(user);
    }

    const payload = {
      sub: user.id,
      role: user.role,
    };

    const accessToken = this.jwtService.sign(payload);

    return {
      accessToken,
      user: {
        id: user.id,
        state: user.state, // Replaced 'status' with 'state'
        role: user.role,
      },
    };
  }
}
