import { Injectable, BadRequestException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { User, UserRole, UserStatus } from '../auth/schemas/user.schema';

@Injectable()
export class OnboardingService {
  constructor(
    @InjectModel(User.name)
    private userModel: Model<User>,
  ) {}

  async setRole(userId: string, role: UserRole) {
    const user = await this.userModel.findById(userId);

    if (!user) throw new BadRequestException('User not found');

    if (user.role) {
      throw new BadRequestException('Role already selected');
    }

    user.role = role;

    if (role === UserRole.PATIENT) {
      user.status = UserStatus.ACTIVE;
    } else {
      user.status = UserStatus.UNDER_REVIEW;
    }

    await user.save();

    return {
      message: 'Role selected successfully',
      role: user.role,
      status: user.status,
    };
  }
}
