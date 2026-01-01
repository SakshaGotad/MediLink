import { IsString, IsDateString } from 'class-validator';

export class CreateAppointmentDto {
  @IsString()
  doctorId: string;

  @IsDateString()
  date: string; // 2025-01-10

  @IsString()
  startTime: string; // 09:00

  @IsString()
  endTime: string; // 09:30
}
