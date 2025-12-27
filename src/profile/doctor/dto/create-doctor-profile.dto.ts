import {
  IsString,
  IsNotEmpty,
  IsNumber,
  Min,
  Max,
  IsOptional,
} from 'class-validator';

export class CreateDoctorProfileDto {
  @IsString()
  @IsNotEmpty()
  fullName: string;

  @IsString()
  @IsNotEmpty()
  specialization: string;

  @IsNumber()
  @Min(0)
  @Max(60)
  experienceYears: number;

  @IsString()
  @IsNotEmpty()
  licenseNumber: string;

  @IsOptional()
  @IsString()
  clinicName?: string;
}
