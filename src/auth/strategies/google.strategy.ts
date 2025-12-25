import { Inject, Injectable } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { Strategy, VerifyCallback } from 'passport-google-oauth20';
import type { ConfigType } from '@nestjs/config';

import googleOauthConfig from 'src/config/google-oauth.config';

@Injectable()
export class GoogleStrategy extends PassportStrategy(Strategy, 'google') {
  constructor(
    @Inject(googleOauthConfig.KEY)
    private readonly googleConfig: ConfigType<typeof googleOauthConfig>,
  ) {
    super({
      clientID: googleConfig.clientID || '', // Ensure valid string
      clientSecret: googleConfig.clientSecret || '', // Ensure valid string
      callbackURL: googleConfig.callbackURL || '', // Ensure valid string
      scope: ['email', 'profile'],
    });
  }

  validate(
    accessToken: string,
    refreshToken: string,
    profile: { id: string; emails: { value: string }[] }, // Added explicit type
    done: VerifyCallback,
  ) {
    const { id, emails } = profile;

    const user = {
      provider: 'GOOGLE',
      provider_user_id: id,
      email: emails?.[0]?.value,
    };

    done(null, user); // Removed unnecessary await
  }
}
