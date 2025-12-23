import { MigrationInterface, QueryRunner } from "typeorm";

export class InitAuth1766479952255 implements MigrationInterface {
    name = 'InitAuth1766479952255'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`CREATE TYPE "public"."auth_providers_provider_enum" AS ENUM('EMAIL', 'GOOGLE')`);
        await queryRunner.query(`CREATE TABLE "auth_providers" ("id" uuid NOT NULL DEFAULT uuid_generate_v4(), "user_id" uuid NOT NULL, "provider" "public"."auth_providers_provider_enum" NOT NULL, "provider_user_id" character varying(255) NOT NULL, "password_hash" character varying, "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(), CONSTRAINT "PK_cb277e892a115855fc95c373422" PRIMARY KEY ("id"))`);
        await queryRunner.query(`CREATE UNIQUE INDEX "IDX_23ce9b23329d07057ec8ece6f2" ON "auth_providers" ("provider", "provider_user_id") `);
        await queryRunner.query(`ALTER TABLE "auth_providers" ADD CONSTRAINT "FK_262996fd08ab5a69e85b53d0055" FOREIGN KEY ("user_id") REFERENCES "users"("id") ON DELETE CASCADE ON UPDATE NO ACTION`);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "auth_providers" DROP CONSTRAINT "FK_262996fd08ab5a69e85b53d0055"`);
        await queryRunner.query(`DROP INDEX "public"."IDX_23ce9b23329d07057ec8ece6f2"`);
        await queryRunner.query(`DROP TABLE "auth_providers"`);
        await queryRunner.query(`DROP TYPE "public"."auth_providers_provider_enum"`);
    }

}
