import { IsEnum, IsString, IsNumber, Min, Max } from 'class-validator';
import { DayOfWeek } from '../availability.schema';

export class CreateAvailabilityDto {
  @IsEnum(DayOfWeek)
  day: DayOfWeek;

  @IsString()
  startTime: string; // "09:00"

  @IsString()
  endTime: string; // "17:00"

  @IsNumber()
  @Min(5)
  @Max(180)
  slotDuration: number;
}
