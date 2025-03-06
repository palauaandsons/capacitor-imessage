import { WebPlugin } from '@capacitor/core';
import type { IMessagePlugin, SendMessageOptions } from './definitions';

export class IMessageWeb extends WebPlugin implements IMessagePlugin {

  async isMessagingAvailable(): Promise<{ available: boolean }> {
    console.warn('isMessagingAvailable is not supported on the web.');
    return Promise.resolve({ available: false });
  }
    
  async sendMessage(_options: SendMessageOptions): Promise<{ status: string }> {
    console.warn('sendMessage is not supported on the web. This only works on iOS.');
    return Promise.reject({ status: "unsupported", message: "iMessage is only available on iOS." });
  }
}
