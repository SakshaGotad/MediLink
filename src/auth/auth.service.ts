/* eslint-disable prettier/prettier */
import { ConflictException, Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { JwtService } from '@nestjs/jwt';
import { User, UserRole, UserStatus } from './schemas/user.schema';


@Injectable()
export class AuthService {
  constructor(
    @InjectModel(User.name) private userModel: Model<User>,
    private jwtService: JwtService,
  ) {}

  async handleGoogleOauth(oauthUser: {
    googleId: string;
    email: string;
    name?: string;
    picture?: string;
  }) {
    let user = await this.userModel.findOne({ googleId: oauthUser.googleId });
  
    if (!user) {
      user = await this.userModel.create({
        googleId: oauthUser.googleId,
        email: oauthUser.email,
        status: UserStatus.ONBOARDING,
      });
    }
  
    const token = this.jwtService.sign({
      sub: user._id,
      role: user.role,
      status: user.status,
    });
  
    return {
      accessToken: token,
      user: {
        id: user._id,
        email: user.email,
        role: user.role,
        status: user.status,
      },
    };
  }



  async setUserRole(userId:string, role:UserRole){
    const user = await this.userModel.findById(userId);
    if(!user){
      throw new NotFoundException("user not found")
    }
    if(user.role){
      throw new ConflictException("role already set")
    }
    user.role = role;
    if (role === UserRole.PATIENT) {
    user.status = UserStatus.ACTIVE;
  } else if (role === UserRole.DOCTOR) {
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
