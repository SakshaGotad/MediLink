import { IsEnum } from 'class-validator';
import { UserRole } from 'src/auth/schemas/user.schema';

export class SelectRoleDto {
  @IsEnum(UserRole)
  role: UserRole;
}
