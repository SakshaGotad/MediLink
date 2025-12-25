import { IsString } from 'class-validator';

export class GoogleOauthDto {
  @IsString()
  idToken: string;
}
