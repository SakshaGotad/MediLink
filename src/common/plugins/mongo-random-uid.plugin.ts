/* eslint-disable @typescript-eslint/no-unsafe-member-access */
import mongoose, { CallbackWithoutResultAndOptionalError } from 'mongoose';
import { RandomUIDUtils } from '../utils/gen-random-uids';

export const randomUidPlugin = (prefix: string) => {
  return (schema: mongoose.Schema) => {
    schema.add({
      uid: {
        type: String,
        unique: true,
        index: true,
      },
    });

    schema.pre(
      'validate',
      function (this: any, next: CallbackWithoutResultAndOptionalError) {
        if (!this.uid) {
          this.uid = RandomUIDUtils.generateRandomUid(prefix);
        }
        next();
      },
    );
  };
};
