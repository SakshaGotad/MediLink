import { IsEnum } from 'class-validator';
import { UserRole } from '../schemas/user.schema';

export class SelectRoleDto {
  @IsEnum(UserRole)
  role: UserRole;
}
