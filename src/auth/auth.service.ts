/* eslint-disable @typescript-eslint/no-unsafe-member-access */
import { BadRequestException, Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { User } from './schemas/auth.entity';
import { Repository } from 'typeorm';
import { AuthProvider, AuthProviderType } from './schemas/auth_provider.entity';
import { SignupDto } from './dto/signup.dto';
import { UserRole, UserStatus } from './schemas/auth.entity';
import * as bcrypt from 'bcrypt';
@Injectable()
export class AuthService {
  constructor(
    @InjectRepository(User)
    private readonly userRepository: Repository<User>,
    private readonly authProviderRepository: Repository<AuthProvider>,
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
      // eslint-disable-next-line prettier/prettier, @typescript-eslint/no-unsafe-call
      passwordHash = await bcrypt.hash(password, 10) as string;
    } catch {
      throw new Error('Error hashing password');
    }

    const user = this.userRepository.create({
      email: email,
      password: passwordHash,
      role: UserRole.PATIENT, // Example role
      state: UserStatus.ONBOARDING, // Example state
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
}
