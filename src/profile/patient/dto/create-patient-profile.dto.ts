/* eslint-disable prettier/prettier */
import { IsEnum, IsNumber, IsOptional, IsString, Min, Max } from 'class-validator';

export enum PatientGender {
  MALE = 'male',
  FEMALE = 'female',
  OTHER = 'other',
}

export class CreatePatientProfileDto {
  @IsString()
  fullName: string;

  @IsOptional()
  @IsNumber()
  @Min(0)
  @Max(120)
  age?: number;

  @IsOptional()
  @IsEnum(PatientGender)
  gender?: PatientGender;
}
