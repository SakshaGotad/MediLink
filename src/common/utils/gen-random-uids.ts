export class RandomUIDUtils {
  static generateRandomUid(prefix: string): string {
    const random = Math.random().toString(36).substring(2, 8).toUpperCase();
    const timestamp = Date.now().toString().slice(-4);
    return `${prefix}_${random}${timestamp}`;
  }
}
