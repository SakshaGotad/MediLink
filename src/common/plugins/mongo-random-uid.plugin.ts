import mongoose from 'mongoose';
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

    schema.pre('validate', function () {
      if (!this.uid) {
        this.uid = RandomUIDUtils.generateRandomUid(prefix);
      }
    });
  };
};
