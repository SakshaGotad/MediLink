import { DataSource } from 'typeorm';
import * as dotenv from 'dotenv';

dotenv.config();

export default new DataSource({
  type: 'postgres',
  url: process.env.PG_URI,

  entities: ['src/**/schemas/*.entity.ts'],
  migrations: ['src/migrations/*.ts'],

  ssl: {
    rejectUnauthorized: false,
  },
});
